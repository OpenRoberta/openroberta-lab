function CreateRestorePoint(name, type, event)
{
    try
    {
        var srObject = GetObject("winmgmts:\\\\.\\root\\default:Systemrestore");
        if (srObject == null)
        {
            //Cannot get System Restore service WMI object
            return 0;
        }
        var srStatus = srObject.CreateRestorePoint(name, type, event);
        return srStatus;
    }
    catch (e)
    {
        //Exception during the restore point creation
        return -1;
    }
}

function BeginDriverInstallation()
{
    var srPointName = "Drivers Installation";
    var DEVICE_DRIVER_INSTALL = 10;
    var BEGIN_NESTED_SYSTEM_CHANGE = 102;
    CreateRestorePoint(srPointName, DEVICE_DRIVER_INSTALL, BEGIN_NESTED_SYSTEM_CHANGE);
    return 0;
}

function EndDriverInstallation()
{
    var srPointName = "Drivers Installation";
    var DEVICE_DRIVER_INSTALL = 10;
    var END_NESTED_SYSTEM_CHANGE = 103;
    CreateRestorePoint(srPointName, DEVICE_DRIVER_INSTALL, END_NESTED_SYSTEM_CHANGE);
    return 0;
}