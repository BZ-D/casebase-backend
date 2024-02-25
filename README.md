# caseBase_backEnd

## 银保监会案例库后端

### 运行方法

- 在src/main/resources/application.properies中
    - 默认设置则使用我们提供的远程数据库，直接启动即可
    - 使用本地数据库则将datasource的配置改为自己本地的配置
        - 需要现场爬取数据则将application.properies的clawer设为true，启动项目会自动建表爬取数据，爬取大概2-3个小时，但过程中可以提供服务
        - 也可以直接导入提供的数据库备份文件，将application.properies的clawer设为false
- 运行src/main/java/com.example.caseBase/CaseBaseApplication即可启动后端
    - 爬取数据过程中有相关报错，不影响运行的情况下可以直接忽略
    - 该项目搜索引擎相关部分位于caseBase包下的util/ESUtil，如需研究可以自行查看
- src以外的文件夹为webService遗留代码，不影响后端启动