package lejos.internal.ev3;


import java.io.*;
import lejos.hardware.Audio;
import lejos.internal.io.NativeDevice;
import lejos.internal.io.SystemSettings;
import lejos.utility.Delay;

/**
 * EV3 sound routines.
 *
 */
public class EV3Audio implements Audio
{

    private static final int RIFF_HDR_SIZE = 44;
    private static final int RIFF_RIFF_SIG = 0x52494646;
    private static final int RIFF_WAVE_SIG = 0x57415645;
    private static final int RIFF_FMT_SIG = 0x666d7420;
    private static final short RIFF_FMT_PCM = 0x0100;
    private static final short RIFF_FMT_1CHAN = 0x0100;
    private static final short RIFF_FMT_8BITS = 0x0800;
    private static final int RIFF_DATA_SIG = 0x64617461;
    
    private static final int PCM_BUFFER_SIZE = 250;
    
    public static final String VOL_SETTING = "lejos.volume";
    
    private static int masterVolume = 0;
    
    private static NativeDevice dev = new NativeDevice("/dev/lms_sound");
    private static final byte OP_BREAK = 0;
    private static final byte OP_TONE = 1;
    private static final byte OP_PLAY = 2;
    private static final byte OP_REPEAT = 3;
    private static final byte OP_SERVICE = 4;
    private static final Audio singleton = new EV3Audio();
    /**
     * Static constructor to force loading of system settings
     */
    static {
        singleton.loadSettings();
    }

    private EV3Audio()
    {
    }

    public static Audio getAudio()
    {
        return singleton;
    }
    
    private static int C2 = 523;
    
    /**
     * Play a system sound.
     * <TABLE BORDER=1>
     * <TR><TH>aCode</TH><TH>Resulting Sound</TH></TR>
     * <TR><TD>0</TD><TD>short beep</TD></TR>
     * <TR><TD>1</TD><TD>double beep</TD></TR>
     * <TR><TD>2</TD><TD>descending arpeggio</TD></TR>
     * <TR><TD>3</TD><TD>ascending  arpeggio</TD></TR>
     * <TR><TD>4</TD><TD>long, low buzz</TD></TR>
     * </TABLE>
     */
    public void systemSound(int aCode)
    {
        if (aCode == BEEP)
            playTone(600, 200);
        else if (aCode == DOUBLE_BEEP)
        {
            playTone(600, 150);
            Delay.msDelay(50);
            playTone(600, 150);
        }
        else if (aCode == ASCENDING)// C major arpeggio
            for (int i = 4; i < 8; i++)
            {
                playTone(C2 * i / 4, 100);
            }
        else if (aCode == DESCENDING)
            for (int i = 7; i > 3; i--)
            {
                playTone(C2 * i / 4, 100);
            }
        else if (aCode == BUZZ)
        {
            playTone(100, 500);
        }
    }

    
    /**
     * Plays a tone, given its frequency and duration. 
     * @param aFrequency The frequency of the tone in Hertz (Hz).
     * @param aDuration The duration of the tone, in milliseconds.
     * @param aVolume The volume of the playback 100 corresponds to 100%
     */
    static void playFreq(int aFrequency, int aDuration, int aVolume)
    {
        if (aVolume >= 0)
            aVolume = (aVolume*masterVolume)/100;
        else
            aVolume = -aVolume;
        byte[] cmd = new byte[6];
        cmd[0] = OP_TONE;
        cmd[1] = (byte) aVolume;
        cmd[2] = (byte) aFrequency;
        cmd[3] = (byte) (aFrequency >> 8);
        cmd[4] = (byte) aDuration;
        cmd[5] = (byte) (aDuration >> 8);
        dev.write(cmd, cmd.length);        
    }


    /**
     * Plays a tone, given its frequency and duration. 
     * @param aFrequency The frequency of the tone in Hertz (Hz).
     * @param aDuration The duration of the tone, in milliseconds.
     * @param aVolume The volume of the playback 100 corresponds to 100%
     */
    public void playTone(int aFrequency, int aDuration, int aVolume)
    {
        playFreq(aFrequency, aDuration, aVolume);
        Delay.msDelay(aDuration);
    }
    

    public void playTone(int freq, int duration)
    {
        playTone(freq, duration, VOL_MAX);
    }


    
    /**
     * Read an LSB format
     * @param d stream to read from
     * @return the read int
     * @throws java.io.IOException
     */
    private int readLSBInt(DataInputStream d) throws IOException
    {
        int val = d.readByte() & 0xff;
        val |= (d.readByte() & 0xff) << 8;
        val |= (d.readByte() & 0xff) << 16;
        val |= (d.readByte() & 0xff) << 24;
        return val;
    }
    /**
     * Play a wav file
     * @param file the 8-bit PWM (WAV) sample file
     * @param vol the volume percentage 0 - 100
     * @return The number of milliseconds the sample will play for or < 0 if
     *         there is an error.
     * @throws FileNotFoundException 
     */
    public int playSample(File file, int vol)
    {
        // First check that we have a wave file. File must be at least 44 bytes
        // in size to contain a RIFF header.
        if (file.length() < RIFF_HDR_SIZE)
            return -9;
        // Now check for a RIFF header
        FileInputStream f = null; 
        DataInputStream d = null;
        try
        {
        	f = new FileInputStream(file);
        	d = new DataInputStream(f);

            if (d.readInt() != RIFF_RIFF_SIG)
                return -1;
            // Skip chunk size
            d.readInt();
            // Check we have a wave file
            if (d.readInt() != RIFF_WAVE_SIG)
                return -2;
            if (d.readInt() != RIFF_FMT_SIG)
                return -3;
            int offset = 16;
            // Now check that the format is PCM, Mono 8 bits. Note that these
            // values are stored little endian.
            int sz = readLSBInt(d);
            if (d.readShort() != RIFF_FMT_PCM)
                return -4;
            if (d.readShort() != RIFF_FMT_1CHAN)
                return -5;
            int sampleRate = readLSBInt(d);
            d.readInt();
            d.readShort();
            if (d.readShort() != RIFF_FMT_8BITS)
                return -6;
            // Skip any data in this chunk after the 16 bytes above
            sz -= 16;
            offset += 20 + sz;
            while (sz-- > 0)
                d.readByte();
            int dataLen;
            // Skip optional chunks until we find a data sig (or we hit eof!)
            for(;;)
            {
                int chunk = d.readInt();
                dataLen = readLSBInt(d); 
                offset += 8;
                if (chunk == RIFF_DATA_SIG) break;
                // Skip to the start of the next chunk
                offset += dataLen;
                while(dataLen-- > 0)
                    d.readByte();
            }
            if (vol >= 0)
                vol = (vol*masterVolume)/100;
            else
                vol = -vol;
            byte []buf = new byte[PCM_BUFFER_SIZE*4+1];
            // get ready to play, set the volume
            buf[0] = OP_PLAY;
            buf[1] = (byte)vol;
            dev.write(buf, 2);
            // now play the file
            buf[1] = 0;
            while((dataLen = d.read(buf, 1, buf.length - 1)) > 0)
            {
                // now make sure we write all of the data
                offset = 0;
                while (offset < dataLen)
                {
                    buf[offset] = OP_SERVICE;
                    int len = dataLen - offset;
                    if (len > PCM_BUFFER_SIZE) len = PCM_BUFFER_SIZE;
                    int written = dev.write(buf, offset, len + 1);
                    if (written == 0)
                    {
                        Delay.msDelay(1);
                    }
                    else
                        offset += written;
                }
            }
        }
        catch (IOException e)
        {
            return -1;
        }
        finally
        {
            try {
                if (d != null)
                    d.close();
                if (f != null)
                    f.close();
            }
            catch (IOException e)
            {
                return -1;
            }                
        }
        return 0;
    }


    /**
     * Play a wav file
     * @param file the 8-bit PWM (WAV) sample file
     * @return The number of milliseconds the sample will play for or < 0 if
     *         there is an error.
     * @throws FileNotFoundException 
     */
    public int playSample(File file)
    {
        return playSample(file, VOL_MAX);
    }


    /**
     * Queue a series of PCM samples to play at the
     * specified volume and sample rate.
     * 
     * @param data Buffer containing the samples
     * @param offset Offset of the first sample in the buffer
     * @param len Number of samples to queue
     * @param freq Sample rate
     * @param vol playback volume
     * @return Number of samples actually queued
     */
    public int playSample(byte [] data, int offset, int len, int freq, int vol)
    {
        if (vol >= 0)
            vol = (vol*masterVolume)/100;
        else
            vol = -vol;
        if (freq != 8000)
            throw new UnsupportedOperationException("Sample rate must be 8KHz");
        int inOffset = offset;
        len += offset;
        byte[] buf = new byte[PCM_BUFFER_SIZE+1];
        // get ready to play, set the volume
        buf[0] = OP_PLAY;
        buf[1] = (byte)vol;
        dev.write(buf, 2);
        buf[1] = 0;
        while (inOffset < len)
        {
            int writeLen = len - inOffset;
            if (writeLen > PCM_BUFFER_SIZE)
                writeLen = PCM_BUFFER_SIZE;
            System.arraycopy(data, inOffset, buf, 1, writeLen);
            buf[0] = OP_SERVICE;
            int written = dev.write(buf, writeLen+1);
            //System.out.println("Written " + written);;
            if (written == 0)
                Delay.msDelay(1);
            else
                inOffset += written;
        }
        return len - offset;
    }

    private int waitUntil(int t)
    {
        int t2;
        while ((t2 = (int) System.currentTimeMillis()) < t)
            Thread.yield();
        return t2;
    }

    /**
     * Play a note with attack, decay, sustain and release shape. This function
     * allows the playing of a more musical sounding note. It uses a set of
     * supplied "instrument" parameters to define the shape of the notes 
     * envelope.
     * @param inst Instrument definition
     * @param freq The note to play (in Hz)
     * @param len  The duration of the note (in ms)
     */
    public void playNote(int[] inst, int freq, int len)
    {
        // Generate an envelope controlled note. The instrument array contains
        // the shape of the envelope. The attack period, the decay period the
        // level to decay to and the level to decay to during the sustain part
        // of the note finally the length of the actual release period. All
        // periods are given in 2000th of a second units. This is because the
        // generation of a tone using the playTone function will often take
        // more than 1ms to execute. This means that the minimum tone segment
        // that we can reliably generate is 2ms and so we use this unit as the
        // basis of note generation.
        int segLen = inst[0];
        // All volume settings are scaled by 100.
        int step = 9000 / segLen;
        int vol = 100;
        int oldVol = 0;
        //System.out.println("Start playing");
        // We do not really have fine grained enough timing so try and keep
        // things aligned as closely as possible to a tick by waiting here
        // before we start for the next tick.
        int t = waitUntil((int) System.currentTimeMillis() + 1);
        // Generate the attack profile from 20 to full volume
        playFreq(freq, len+2000, vol / 100);
        len /= 2;
        for (int i = 0; i < segLen; i++)
        {
            vol += step;
            if (oldVol != vol/100)
            {
                playFreq(freq, 100, vol / 100);
                oldVol = vol/100;
            }
            t = waitUntil(t + 2);
        }
        len -= segLen;
        // Now do the decay
        segLen = inst[1];
        if (segLen > 0)
        {
            step = inst[2] / segLen;
            for (int i = 0; i < segLen; i++)
            {
                vol -= step;
                if (oldVol != vol/100)
                {
                    playFreq(freq, 100, vol / 100);
                    oldVol = vol/100;
                }
                t = waitUntil(t + 2);
            }
            len -= segLen;
        }
        segLen = inst[4];
        len -= segLen;
        // adjust length of the sustain and possibly the release to match the
        // requested note length
        if (len > 0)
        {
            step = inst[3] / len;
            for (int i = 0; i < len; i++)
            {
                vol -= step;
                if (oldVol != vol/100)
                {
                    playFreq(freq, 100, vol / 100);
                    oldVol = vol/100;
                }
                t = waitUntil(t + 2);
            }
        }
        else
            segLen += len;
        // Finally do the release
        if (segLen > 0)
        {
            //System.out.println("RVol: " +vol);
            step = (vol - 100) / segLen;
            for (int i = 0; i < segLen; i++)
            {
                vol -= step;
                if (oldVol != vol/100)
                {
                    playFreq(freq, 100, vol / 100);
                    oldVol = vol/100;
                }
                t = waitUntil(t + 2);
            }
        }
        byte []cmd = new byte[1];
        cmd[0] = OP_BREAK;
        dev.write(cmd, 1);
    }

    /**
     * Set the master volume level
     * @param vol 0-100
     */
    public void setVolume(int vol)
    {
        if (vol > VOL_MAX) vol = VOL_MAX;
        if (vol < 0) vol = 0;
        masterVolume = vol;
    }

    /**
     * Get the current master volume level
     * @return the current master volume 0-100
     */
    public int getVolume()
    {
        return masterVolume;
    }
    
    /**
     * Load the current system settings associated with this class. Called
     * automatically to initialize the class. May be called if it is required
     * to reload any settings.
     */
    public void loadSettings()
    {
        masterVolume = SystemSettings.getIntSetting(VOL_SETTING, 80);
    }
}
