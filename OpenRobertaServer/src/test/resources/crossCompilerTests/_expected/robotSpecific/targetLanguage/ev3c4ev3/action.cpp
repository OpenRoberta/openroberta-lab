#define PROGRAM_NAME "NEPOprog"
#define WHEEL_DIAMETER 5.6
#define TRACK_WIDTH 18.0

#include <ev3.h>
#include <math.h>
#include <list>
#include "NEPODefs.h"


void ____action();
void ____move();
void ____drive();
void ____display();
void ____sounds();
void ____lights();

double ___numberVar = 0;
bool ___booleanVar = true;
std::string ___stringVar = "";
Color ___colourVar = White;
BluetoothConnectionHandle ___connectionVar = NULL;
std::list<double> ___numberList = ((std::list<double>){0, 0});
std::list<bool> ___booleanList = ((std::list<bool>){true, true});
std::list<std::string> ___stringList = ((std::list<std::string>){"", ""});
std::list<Color> ___colourList = ((std::list<Color>){White, White});
std::list<BluetoothConnectionHandle> ___connectionList = ((std::list<BluetoothConnectionHandle>){___connectionVar, ___connectionVar});

void ____action() {
    ____move();
    ____drive();
    ____display();
    ____sounds();
    ____lights();
}

void ____move() {
    OnFwdReg(OUT_A, Speed(___numberVar));
    OnFwdReg(OUT_B, Speed(___numberVar));
    OnFwdReg(OUT_C, Speed(___numberVar));
    OnFwdEx(OUT_D, Speed(___numberVar), RESET_NONE);
    RotateMotorForAngle(OUT_A, Speed(___numberVar), 360 * ___numberVar);
    RotateMotorForAngle(OUT_A, Speed(___numberVar), ___numberVar);
    RotateMotorForAngle(OUT_B, Speed(___numberVar), 360 * ___numberVar);
    RotateMotorForAngle(OUT_B, Speed(___numberVar), ___numberVar);
    RotateMotorForAngle(OUT_C, Speed(___numberVar), 360 * ___numberVar);
    RotateMotorForAngle(OUT_C, Speed(___numberVar), ___numberVar);
    DrawString(ToString(MotorPower(OUT_A)), ___numberVar, ___numberVar);
    DrawString(ToString(MotorPower(OUT_B)), ___numberVar, ___numberVar);
    DrawString(ToString(MotorPower(OUT_C)), ___numberVar, ___numberVar);
    DrawString(ToString(MotorPower(OUT_D)), ___numberVar, ___numberVar);
    SetPower(OUT_A, Speed(___numberVar));
    SetPower(OUT_B, Speed(___numberVar));
    SetPower(OUT_C, Speed(___numberVar));
    SetPower(OUT_D, Speed(___numberVar));
    Float(OUT_A);
    Off(OUT_A);
    Float(OUT_B);
    Off(OUT_B);
    Float(OUT_C);
    Off(OUT_C);
    Float(OUT_D);
    Off(OUT_D);
}

void ____drive() {
    RotateMotorForAngle(OUT_BC, Speed(___numberVar), (___numberVar * 360) / (M_PI * WHEEL_DIAMETER));
    RotateMotorForAngle(OUT_BC, -Speed(___numberVar), (___numberVar * 360) / (M_PI * WHEEL_DIAMETER));
    OnFwdSync(OUT_BC, Speed(___numberVar));
    OnRevSync(OUT_BC, Speed(___numberVar));
    Off(OUT_BC);
    RotateMotorForAngleWithTurn(OUT_BC, Speed(___numberVar), (___numberVar * TRACK_WIDTH / WHEEL_DIAMETER), -200);
    RotateMotorForAngleWithTurn(OUT_BC, Speed(___numberVar), (___numberVar * TRACK_WIDTH / WHEEL_DIAMETER), 200);
    OnFwdSyncEx(OUT_BC, Speed(___numberVar), -200, RESET_NONE);
    OnFwdSyncEx(OUT_BC, Speed(___numberVar), 200, RESET_NONE);
    SteerDriveForDistance(OUT_C, OUT_B, Speed(___numberVar), Speed(___numberVar), ___numberVar);
    SteerDriveForDistance(OUT_C, OUT_B, -Speed(___numberVar), -Speed(___numberVar), ___numberVar);
    SteerDrive(OUT_C, OUT_B, Speed(___numberVar), Speed(___numberVar));
    SteerDrive(OUT_C, OUT_B, -Speed(___numberVar), -Speed(___numberVar));
}

void ____display() {
    DrawString(ToString(___stringVar), ___numberVar, ___numberVar);
    LcdPicture(LCD_COLOR_BLACK, 0, 0, OLDGLASSES);
    LcdPicture(LCD_COLOR_BLACK, 0, 0, EYESOPEN);
    LcdPicture(LCD_COLOR_BLACK, 0, 0, EYESCLOSED);
    LcdPicture(LCD_COLOR_BLACK, 0, 0, FLOWERS);
    LcdPicture(LCD_COLOR_BLACK, 0, 0, TACHO);
    LcdClean();
}

void ____sounds() {
    NEPOPlayTone(___numberVar, ___numberVar);
    NEPOPlayTone(261.626, 2000);
    NEPOPlayTone(293.665, 1000);
    NEPOPlayTone(329.628, 500);
    NEPOPlayTone(349.228, 250);
    NEPOPlayTone(391.995, 125);
    PlaySystemSound(SOUND_CLICK);
    PlaySystemSound(SOUND_DOUBLE_BEEP);
    PlaySystemSound(SOUND_DOWN);
    PlaySystemSound(SOUND_UP);
    PlaySystemSound(SOUND_LOW_BEEP);
    SetVolume(___numberVar);
    DrawString(ToString(GetVolume()), ___numberVar, ___numberVar);
    SetLanguage("de");
    SetLanguage("en");
    SetLanguage("fr");
    SetLanguage("es");
    SetLanguage("it");
    SetLanguage("nl");
    SetLanguage("fi");
    SetLanguage("pl");
    SetLanguage("ru");
    SetLanguage("tu");
    SetLanguage("cs");
    SetLanguage("pt-pt");
    SetLanguage("da");
    Say(ToString(___stringVar), 30, 50);
    Say(ToString(___stringVar), ___numberVar, ___numberVar);
}

void ____lights() {
    SetLedPattern(LED_GREEN);
    SetLedPattern(LED_GREEN_FLASH);
    SetLedPattern(LED_GREEN_PULSE);
    SetLedPattern(LED_ORANGE);
    SetLedPattern(LED_ORANGE_FLASH);
    SetLedPattern(LED_ORANGE_PULSE);
    SetLedPattern(LED_RED);
    SetLedPattern(LED_RED_FLASH);
    SetLedPattern(LED_RED_PULSE);
    SetLedPattern(LED_BLACK);

}

int main () {
    NEPOInitEV3();
    NEPOSetAllSensors(NULL, NULL, NULL, NULL);
    SetLanguage("en");startLoggingThread(OUT_ABCD);


    ____action();

    NEPOFreeEV3();
    return 0;
}
