1. 多线程下载
     步骤:    1. 先发一个请求 method: HEAD    , 以获取要下载的文件的大小.
             2. 利用 java.io.  RandomAccessFile  类来指定文件大小，以创建新的空文件.   作用：在磁盘上占一个位置.
                      随机访问文件类： 读写,按指定的位置访问.   seek(  long position )
             3. 开始下载，创建线程，计算此线程的start,end .拼接协议:  Range: bytes=5001-10000
                    发出请求，下载指定部分.
                    HttpURLConnection.setRequestProperty(请求头域,值)
             4. synchronized   ->    volatile( 有序性，可见性)  ->  long的原子性操作问题 -> java.util.concurrent.AtomicLong
                                                                                    -> int

 扩展：
            1. 下载速度: 下载时间，下载量
     		2. 暂停: 断点续传.
             			暂停:  ->针对线程.        interrupt();
     		3. 百分比: 下载量/总大小
     		4. 充钱提速:     -减速.
            			web端/手机端(数据库) .
            			提速:
             				多个数据源.

              				url: 本来的数据源     qq.exe
             				url2: 另一个数据源.   qqxxx.exe
              				url3: 再来一个数据源    qqyyyy.exe

          	 			如何判断多个数据源是同一个文件?      MD5,sha
                    					原文-MD5 -> 32位编码.

           			数据库:
                       			文件位置                                  MD5码
                      			华军软件园/qqxx.exe                     1x2
        				下载腾迅的qq时，先计算一次MD5码.

     		5. 云下载:
            			在服务器下载， 再由用户一次性下载到本地.
     		6. 多下载方式:
           			磁力下载. ....
     		7. C/S:
         			swing:资料多
        			 swt: