package lejos.hardware.sensor;

import java.util.ArrayList;

import lejos.hardware.Device;


public class BaseSensor extends Device implements SensorModes
{
    protected SensorMode[] modes;
    ArrayList<String> modeList;

    /**
     * Define the set of modes to be made available for this sensor.
     * @param m An array containing a list of modes
     */
    protected void setModes(SensorMode[] m)
    {
        modes = m;
        // force the list to be rebuilt
        modeList = null;
    }

    @Override
    public ArrayList<String> getAvailableModes()
    {
        if (modeList == null)
        {
            modeList = new ArrayList<String>(modes.length);
            if (modes != null)
                for(SensorMode m : modes)
                    modeList.add(m.getName());
        }
        return modeList;
    }

    @Override
    public SensorMode getMode(int mode)
    {
        if (mode < 0)
            throw new IllegalArgumentException("Invalid mode " + mode);
        if (modes == null || mode >= modes.length)
            return null;
        return modes[mode];
    }

    @Override
    public SensorMode getMode(String modeName)
    {
        // TODO: I'm sure there is a better way to do this, but it is late!
        int i = 0;
        for(String s : getAvailableModes())
        {
            if (s.equals(modeName))
                return modes[i];
            i++;
        }
        throw new IllegalArgumentException("No such mode " + modeName);
    }

}
