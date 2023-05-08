#跟著Mark大大鐵人賽的Spring Boot... 深不可測 系列
https://ithelp.ithome.com.tw/users/20152418/ironman/5654
##Mark大大寫在最後一天的心得
這次鐵人賽算是分了兩階段，第一階段是原本的專案開發，第二階段因為要寫文章，所以又開了新的專案在同步原本專案，剪剪貼貼難免會出問題，然後就花比較多的時間在 Debug... 如果還有哪裡有錯，還請各位大大不吝指教，我會盡快修復！
經過這次開發，我覺得以後不要再嘗試用網頁寫遊戲好了，這真的不是個好主意，雖然我在第一天就知道了...　不過在意料之外的是，我居然能連續寫完 30 天，畢竟我到第 10 天就開始覺得膩了，但還是硬著頭皮寫完，加上最近還有其他事情在忙，每天都在燃燒，不過完成鐵人賽還是滿有成就感的~~
小弟還留了些東西讓讀者自由發揮的空間，比如：房間和結算頁面的頭貼、錯誤訊息的反饋等，因為不是在這次功能裡特別重要的，所以就先跳過~~
最後，說下小弟在技術相關的疑問好了，希望有大大能解釋~~
1. Component 的實例，究竟還需不需要 Service 負責存取？
2. 不太確定為什麼每輪出牌都會有這麼高的延遲，是 WebSocket 的問題嗎？

##跟著實作時遇到的問題
###SpringBoot太多版本有可能導致專案跑不起來
I'd suggest grooming your pom by:
* Remove versions of dependencies that Spring Boot manages.
* Remove dependencies that are already brought in transitively by Spring Boot starters.
https://stackoverflow.com/questions/59878437/cannot-instantiate-interface-org-springframework-context-applicationcontextiniti

###解決javax.validation.ValidationException: HV000028: Unexpected exception during isValid call.問題的方法
I solved by adding this to application.propperties
```propperties
spring.jpa.properties.javax.persistence.validation.mode=none
```
https://stackoverflow.com/questions/51130391/what-can-cause-a-hv000028-validation-exception

#####不確定有沒有效，不過可以參考一下在pom檔validation依賴加上長期支援版本的指定
解决:javax.validation.ValidationException
https://blog.csdn.net/qq_25591259/article/details/104302797?spm=1001.2101.3001.6650.3&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-3-104302797-blog-122927725.235%5Ev28%5Epc_relevant_recovery_v2&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-3-104302797-blog-122927725.235%5Ev28%5Epc_relevant_recovery_v2&utm_relevant_index=4

##筆記的地方
###WebSocket全雙工即時傳輸
瀏覽器和伺服器雙方握手後，及同意即時傳輸訊息，常用於多人聊天室或多人線上遊戲
Spring Boot WebSocket 建立一個簡單的網頁聊天室
https://matthung0807.blogspot.com/2019/04/spring-boot-websocket_27.html
SpringBoot - 第二十八章 | WebSocket簡介及應用
https://morosedog.gitlab.io/springboot-20190416-springboot28/

###java.security.Principal
https://matthung0807.blogspot.com/2018/03/spring-security-principal.html

###Timer與ScheduledExecutorService的區別
Timer雖然有倒數計時和計時的功能，但在遊戲中需要的是一出牌就停止計時，因此這邊不適用；
ScheduledExecutorService一樣也是倒數計時和計時功能，不過可以透過awaitTermination()
的設定，等前一個計時結束後，開始下一個倒數計時
Java学习：Timer与ScheduledExecutorService的区别
https://blog.csdn.net/nalw2012/article/details/49633413

###difference between System.out::println and System.out.println
https://coderanch.com/t/699012/java/difference-System-println-System-println

###啟動時報錯`Failed to configure a DataSource: 'url' attribute is not specified and no embedded datasource could be configured.`
`application.properties`中少添加了資料庫名稱
```properties
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```
解決SpringBoot 項目啟動報錯Failed to configure a DataSource: 'url' attribute is not specified and no embed
https://blog.csdn.net/weixin_43487782/article/details/115496746

####用lombok的Getter和Setter時，產生重複名稱的method也會跳出這個錯誤

###原作者說遊玩時速度比較慢，是否是每個花色的檢驗問題？是否有更快速、不用每次都全跑完的檢驗方法？
是否是出 5 張牌？

數字是否連續？

花色是否相同？

是否有 4 張重複的？

是否有 3 張重複的和 2 張重複的？

是否只有出 2 張同數字的？

###Lombok在Eclipse需要在官網下載另外安裝
###### mac : https://yuan-0708.medium.com/lombok%E5%AE%89%E8%A3%9D-1a8c67eecae2
###### windows : https://matthung0807.blogspot.com/2019/09/eclipse-lombok-installation.html

