Windows 10
--------------------
2022-02-12 10:52:43 078
---------------------
    true false 0 10 0
    false false 1 10 2
    false false 2 10 4
    false false 3 10 6
    false false 4 10 8
    false false 5 10 10
    false false 6 10 12
    false false 7 10 14
    false false 8 10 16
    false true 9 10 18
---------------------
012-2.13
---------------------
3
----------------------
C:\Program Files (x86)\Python36-32\Scripts\;C:\Program Files (x86)\Python36-32\;C:\Program Files (x86)\Common Files\Oracle\Java\javapath;C:\Java\jdk1.8.0_201\bin;C:\Java\jdk1.8.0_201\jre\bin;C:\CMDTOOLS;C:\FFMPEG\dll;D:\scrcpy-win32-v1.13;C:\Windows\system32;C:\Windows;C:\Windows\System32\Wbem;C:\Windows\System32\WindowsPowerShell\v1.0\;C:\ADB;C:\Go;.;\bin;C:\Program Files (x86)\NVIDIA Corporation\PhysX\Common;C:\Program Files\Redis\;C:\Program Files\TortoiseSVN\bin;C:\Program Files (x86)\Windows Kits\8.1\Windows Performance Toolkit\;C:\Program Files\Microsoft SQL Server\110\Tools\Binn\;E:\Programs\NodeJs;E:\Programs\NodeJs\node_global;C:\OpenCV\opencv\build\bin;C:\OpenCV\opencv\build\x64\vc14\bin;C:\OpenCV\opencv\build\x64\vc15\bin;C:\Program Files\MySQL\MySQL Shell 8.0\bin\
----------------------
path:
{->C:\Program Files (x86)\Python36-32\Scripts\
->C:\Program Files (x86)\Python36-32\
->C:\Program Files (x86)\Common Files\Oracle\Java\javapath
->C:\Java\jdk1.8.0_201\bin
->C:\Java\jdk1.8.0_201\jre\bin
->C:\CMDTOOLS
->C:\FFMPEG\dll
->D:\scrcpy-win32-v1.13
->C:\Windows\system32
->C:\Windows
->C:\Windows\System32\Wbem
->C:\Windows\System32\WindowsPowerShell\v1.0\
->C:\ADB
->C:\Go
->.
->\bin
->C:\Program Files (x86)\NVIDIA Corporation\PhysX\Common
->C:\Program Files\Redis\
->C:\Program Files\TortoiseSVN\bin
->C:\Program Files (x86)\Windows Kits\8.1\Windows Performance Toolkit\
->C:\Program Files\Microsoft SQL Server\110\Tools\Binn\
->E:\Programs\NodeJs
->E:\Programs\NodeJs\node_global
->C:\OpenCV\opencv\build\bin
->C:\OpenCV\opencv\build\x64\vc14\bin
->C:\OpenCV\opencv\build\x64\vc15\bin
->C:\Program Files\MySQL\MySQL Shell 8.0\bin\
}

92bf7e46-d643-44d5-8c9b-3c0c0127e500
690E5C84F3304E0CB7E0C9C35E3B7B23

files:[
D:\IDEA_ROOT\i2f-commons\i2f-component\src\test\java\gen\tpl\controller
D:\IDEA_ROOT\i2f-commons\i2f-component\src\test\java\gen\tpl\dao
D:\IDEA_ROOT\i2f-commons\i2f-component\src\test\java\gen\tpl\model
D:\IDEA_ROOT\i2f-commons\i2f-component\src\test\java\gen\tpl\service
D:\IDEA_ROOT\i2f-commons\i2f-component\src\test\java\gen\tpl\Tool.js.vm
]

            dir:D:\IDEA_ROOT\i2f-commons\i2f-component\src\test\java\gen\tpl\controller
                dir:D:\IDEA_ROOT\i2f-commons\i2f-component\src\test\java\gen\tpl\dao
                dir:D:\IDEA_ROOT\i2f-commons\i2f-component\src\test\java\gen\tpl\model
                dir:D:\IDEA_ROOT\i2f-commons\i2f-component\src\test\java\gen\tpl\service
                file:D:\IDEA_ROOT\i2f-commons\i2f-component\src\test\java\gen\tpl\Tool.js.vm
    

${_vm.sysProperty("os.name")}
--------------------
${_vm.format(${_vm.now()},"yyyy-MM-dd HH:mm:ss SSS")}
---------------------
#foreach($item in ${_vm.list(${_vm.fori(0,20,2)})})
    ${item.first} ${item.last} ${item.index} ${item.size} ${item.value}
#end
---------------------
${_vm.format("%03d-%.02f",12,2.125)}
---------------------
${_vm.randInt(3,5)}
----------------------
${_vm.sysEnv("path")}
----------------------
${_vm.join(${_vm.split(${_vm.sysEnv("path")},";",-1)},"->%s","
","path:
{","
}")}

${_vm.uuid()}
${_vm.replaceAll(${_vm.uuid()},"-","").toUpperCase()}

${_vm.join(${_vm.listFiles("D:\IDEA_ROOT\i2f-commons\i2f-component\src\test\java\gen\tpl")},null,"
","files:[
","
]")}

#foreach($file in ${_vm.listFiles("D:\IDEA_ROOT\i2f-commons\i2f-component\src\test\java\gen\tpl")})
    #if($file.isFile())
        file:${file.getAbsolutePath()}
    #elseif($file.isDirectory())
        dir:${file.getAbsolutePath()}
    #end
#end


${_vm.readFile("D:\IDEA_ROOT\i2f-commons\i2f-component\src\test\java\gen\tpl\Tool.js.vm","UTF-8")}

${_vm.cmdResult("ping 114.114.114.114","GBK")}



正在 Ping 114.114.114.114 具有 32 字节的数据:
来自 114.114.114.114 的回复: 字节=32 时间=51ms TTL=72
来自 114.114.114.114 的回复: 字节=32 时间=49ms TTL=87
来自 114.114.114.114 的回复: 字节=32 时间=51ms TTL=79
来自 114.114.114.114 的回复: 字节=32 时间=48ms TTL=76

114.114.114.114 的 Ping 统计信息:
    数据包: 已发送 = 4，已接收 = 4，丢失 = 0 (0% 丢失)，
往返行程的估计时间(以毫秒为单位):
    最短 = 48ms，最长 = 51ms，平均 = 49ms

