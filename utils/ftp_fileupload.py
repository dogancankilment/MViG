import ftplib
import os


def ftp_upload():
    ftp = ftplib.FTP("www.mefedck.hol.es")
    ftp.login("u439637200", "12345678dck")
    upload_file = for_file + "/app/templates/app/index.html"
    ftp.storlines("STOR " + os.path.basename(upload_file), open(upload_file))


if __name__ == '__main__':
    for_file = os.path.dirname(os.path.abspath(os.path.dirname(__file__)))
    ftp_upload()
