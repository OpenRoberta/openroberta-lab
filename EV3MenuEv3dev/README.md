# intro #
A connector for the open roberta lab for the lego mindstroms ev3 running the
firmware from ev3dev.org and ev3dev-lang-python.

# how to #
https://mp-devel.iais.fraunhofer.de/wiki/display/ORDevel/Open+Roberta+on+ev3dev

## prerequisites ##
python-ev3dev
python-bluez
python-dbus

## dist ##
``python setup.py sdist``

or old fashioned:
``find . -name '*.py' -o -name '*.pbm' -o -name '*.pil' | tar -cvjf ev3dev-robertalab.tar.bz2 --files-from -``

## upload to ev3 ##
``scp -r robertalab.py roberta root@ev3dev.local:/home/user``

The location of the robertalab-server can be configured by placing a
robertalab.json next to the robertalab.py, e.g.:
``
{
    "target": "http://localhost:1999"
}
``

## start it ##
Go to the ``File Browser``, navigate to ``/home/user`` and select robertalab.py.
Leave the connector with a long press of the ``back`` button.
