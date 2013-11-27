Blocking server will serve a given file regardless of the resource requested and will keep the connection open after sending the file until the user releases it by pressing Q + ENTER. 

This server can be used to weaponize XXE and SSRF attacks and upload arbitrary files to the server. Note that the attcker wont be able to control the upload directory, the name nor the extension, so other vulnerabilities may be required for a successful attack.

Credits go to Timothy D. Morgan (@ecbftw) and his great talk on XXE during OWASP AppSec US 2013:
[Video](http://www.youtube.com/watch?v=eHSNT8vWLfc&feature=youtu.be)

# Usage:

javac BlockingServer.java

java BlockingServer <port> <file to serve>

Press Q and ENTER when you want to release the connection


