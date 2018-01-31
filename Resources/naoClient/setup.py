from setuptools import setup

setup(name='naoclient',
      version='0.0.1',
      description='A package that contains a REST client for NAO to communicate with OpenRoberta server.',
      url='https://github.com/OpenRoberta/robertalab',
      author='Artem Vinokurov',
      author_email='artem.vinokurov@iais.fraunhofer.de',
      license='GPL',
      packages=['naoclient'],
      install_requires=[
          'requests',
          ],
      zip_safe=False)
