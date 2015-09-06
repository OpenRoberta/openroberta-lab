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
``VERSION="1.2.0" python setup.py sdist``
This is wired to be called from ``mvn install`` where the version is taken from
the pom.xml.

## upload to ev3 ##
``scp -r robertalab.py roberta root@ev3dev.local:/home/user``

This would be using the packge but is still not working as intended.
``
scp dist/robertalab-1.2.0-SNAPSHOT.tar.gz root@ev3dev.local:/tmp/
pip install --no-deps --force-reinstall --target /home/user --install-option="--install-scripts=/home/user" /tmp/robertalab-1.2.0-SNAPSHOT.tar.gz
``

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
