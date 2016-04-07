import ftplib


def directory_list():
    ftp = ftplib.FTP("www.mefedck.hol.es")
    ftp.login("u439637200", "12345678dck")
    data = []
    ftp.dir(data.append)
    ftp.quit()

    for line in data:
        print "-", line

if __name__ == '__main__':
    directory_list()
