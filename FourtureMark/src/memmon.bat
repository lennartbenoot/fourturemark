@ECHO OFF
echo Memory Monitor - DO NOT CLOSE

:newcycle

echo "" >tasklist.txt

tasklist /FO CSV | find "chrome.exe" >>tasklist.txt
tasklist /FO CSV | find "iexplore.exe" >>tasklist.txt
tasklist /FO CSV | find "firefox.exe" >>tasklist.txt

for /f %%a in (tasklist.txt) do (
	"c:\Program Files\GnuWin32\bin\wget" -O NULL http://fourturemark.appspot.com/memmonServlet?p=%COMPUTERNAME%,%%a
)

timeout 600
goto newcycle