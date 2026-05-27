import sys
with open('src/main/java/com/bbm4/view/MainView.java', 'rb') as f:
    b = f.read()
if b.startswith(b'\xef\xbb\xbf'):
    b = b[3:]
with open('src/main/java/com/bbm4/view/MainView.java', 'wb') as f:
    f.write(b)
