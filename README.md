# Nbp api test app

Application is downloading historic values of currencies from NBP public api (http://api.nbp.pl/api/exchangerates).

	- When you start application with empty database only available option is button "Db downloader"
	- After clicking it will appear window for choosing date range of currencies to download (Note that NBP provide data only for past 93 days)
	- Currently downloaded data range if above datepicker fields
	- After choosing date range click button "Get New Currency Data" and when downloading is finished notification "Data saved in database" and you can close this window
	- In main window you can choose one or many curriencies to show by choosing from list on the left with ctrl keyboard button
	- You can also choose dates to show, but only in range downloaded in database
	- To draw chart click button "Draw" and to clear layout click button "Clear"
	- Next chart is appearing under last one
	