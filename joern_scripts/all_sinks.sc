def source = {
    println("Getting source...")
    cpg.method.isConstructor.signatureExact("void(java.lang.String)").parameter.order(1).l
}


def sink_by_arg = {
    (
        // Command injection
           cpg.method.fullName(".*java\\.lang\\.Runtime\\.exec:java\\.lang\\.Process\\(java\\.lang\\.String\\)")
        // File Manipulation
        ++ cpg.method.fullName(".*java\\.io\\.FileInputStream\\.<init>:.*")
        ++ cpg.method.fullName(".*java\\.io\\.FileReader\\.<init>:.*")
        ++ cpg.method.fullName(".*java\\.io\\.RandomAccessFile\\.<init>:.*")
        ++ cpg.method.fullName(".*java\\.nio\\.file\\.Files.*")
        ++ cpg.method.fullName(".*java\\.io\\.File\\..*")
        ++ cpg.method.fullName(".*java\\.io\\.FileWriter.*")
        ++ cpg.method.fullName(".*java\\.io\\.FileOutputStream.*")
        // JDNI Injection
        ++ cpg.method.fullName(".*javax\\.naming\\.Context\\.lookup:java\\.lang\\.Object\\(java\\.lang\\.String\\)")
        ++ cpg.method.fullName(".*org\\.apache\\.commons\\.text\\.lookup\\.StringLookup\\.lookup:java\\.lang\\.String\\(java\\.lang\\.String\\)")
        // Expression Language injection - MVEL
        ++ cpg.method.fullName(".*\\.mvel2\\.MVEL\\.executeExpression:.*")
        ++ cpg.method.fullName(".*\\.mvel2\\.MVEL\\.eval.*")
        // Expression Language injection - SpEL
        ++ cpg.method.fullName(".*org\\.springframework\\.expression\\.ExpressionParser\\.parseExpression:.*")
        // Expression Language injection - EL
        ++ cpg.method.fullName(".*javax\\.el\\.ExpressionFactory\\.createValueExpression:.*")
        ++ cpg.method.fullName(".*jakarta\\.el\\.ExpressionFactory\\.createValueExpression:.*")
        // Expression Language injection - JEXL
        ++ cpg.method.fullName(".*org\\.apache\\.commons\\.jexl\\d\\.JexlEngine\\.createExpression:.*")
        ++ cpg.method.fullName(".*org\\.apache\\.commons\\.jexl\\d\\.JexlEngine\\.createScript:.*")
        // JDBC Connect
        ++ cpg.method.fullName(".*DriverManager\\.getConnection.*")
        ++ cpg.method.fullName(".*Driver\\.connect.*")
        // Spring XML Bean reader
        ++ cpg.method.fullName(".*org\\.springframework\\.beans\\.factory\\.xml\\.XmlBeanDefinitionReader\\.loadBeanDefinitions:.*")
        // Unsafe Deserialization
        ++ cpg.method.fullName(".*XStream.*fromXML.*")
        // SSRF
        ++ cpg.method.fullName(".*\\.[^\\.]*connect[^\\.]*:.*")
        ++ cpg.method.fullName(".*java\\.net\\.URI\\.<init>:void\\(java\\.lang\\.String\\)")
        ++ cpg.method.fullName(".*java\\.net\\.URL\\.<init>:void\\(java\\.lang\\.String\\)")
        ++ cpg.method.fullName(".*\\.[^\\.]*(?i)setur[il][^\\.]*:.*")
        ++ cpg.method.fullName(".*\\.[^\\.]*(?i)ur[il][^\\.]*:.*")
        // XXE
        ++ cpg.method.fullName(".*javax\\.xml\\.parsers\\.DocumentBuilder\\.parse:.*")
        ++ cpg.method.fullName(".*javax\\.xml\\.parsers\\.SAXParser\\.parse:.*")
        ++ cpg.method.fullName(".*org\\.jdom\\d?\\.input\\.SAXBuilder\\.build:.*")
        ++ cpg.method.fullName(".*org\\.dom4j\\.io\\.SAXReader\\.read:.*")
        ++ cpg.method.fullName(".*org\\.xml\\.sax\\.XMLReader\\.parse:.*")
        ++ cpg.method.fullName(".*javax\\.xml\\.validation\\.Validator\\.validate:.*")
        ++ cpg.method.fullName(".*javax\\.xml\\.validation\\.SchemaFactory\\.newSchema:.*")
        ++ cpg.method.fullName(".*javax\\.xml\\.transform\\.Transformer\\.transform:.*")
        ++ cpg.method.fullName(".*org\\.xml\\.sax\\.helpers\\.XMLReaderAdapter\\.parse:.*")
        // SQL Injection
        ++ cpg.method.fullName(".*java\\.sql\\.Statement\\.executeUpdate:.*")
        ++ cpg.method.fullName(".*java\\.sql\\.Statement\\.executeLargeUpdate:.*")
        ++ cpg.method.fullName(".*java\\.sql\\.Statement\\.executeQuery:.*")
        ++ cpg.method.fullName(".*java\\.sql\\.Statement\\.execute:.*")
        ++ cpg.method.fullName(".*java\\.sql\\.Statement\\.addBatch:.*")
        ++ cpg.method.fullName(".*java\\.sql\\.Connection\\.prepareStatement:.*")
        ++ cpg.method.fullName(".*java\\.sql\\.Connection\\.prepareCall:.*")
        ++ cpg.method.fullName(".*javax\\.persistence\\.EntityManager\\.createNativeQuery:.*")
        ++ cpg.method.fullName(".*javax\\.persistence\\.EntityManager\\.createQuery:.*")
        ++ cpg.method.fullName(".*org\\.hibernate\\.Session\\.createQuery:.*")
        ++ cpg.method.fullName(".*org\\.hibernate\\.Session\\.createSQLQuery:.*")
        ++ cpg.method.fullName(".*javax\\.jdo\\.PersistenceManager\\.newQuery:.*")
        ++ cpg.method.fullName(".*javax\\.jdo\\.Query\\.setFilter:.*")
        ++ cpg.method.fullName(".*javax\\.jdo\\.Query\\.setGrouping:.*")
        ++ cpg.method.fullName(".*org\\.springframework\\.jdbc\\.core\\.JdbcOperations\\.execute:.*")
        ++ cpg.method.fullName(".*org\\.springframework\\.jdbc\\.core\\.JdbcOperations\\.query:.*")
        ++ cpg.method.fullName(".*org\\.springframework\\.jdbc\\.core\\.JdbcOperations\\.update:.*")
        ++ cpg.method.fullName(".*org\\.springframework\\.jdbc\\.core\\.PreparedStatementCreatorFactory\\.newPreparedStatementCreator:.*")
        ++ cpg.method.fullName(".*org\\.springframework\\.jdbc\\.core\\.PreparedStatementCreatorFactory\\.<init>:.*")
        // Load code
        ++ cpg.method.fullName(".*java\\.lang\\.System\\.load:.*")
        ++ cpg.method.fullName(".*java\\.lang\\.System\\.loadLibrary:.*")
        ++ cpg.method.fullName(".*java\\.net\\.URLClassLoader.*")
        ++ cpg.method.fullName(".*javax\\.management\\.loading\\.MLet.*")
        // Code exec
        ++ cpg.method.fullName(".*javax\\.script\\.ScriptEngine\\.eval:.*")


    ).callIn.argument.filter(_.argumentIndex > 0).l
}

def sink_by_this = {
    (
        // Command injection
           cpg.method.fullName(".*java\\.lang\\.ProcessBuilder\\.start:java\\.lang\\.Process\\(\\)")
        // Unsafe Deserialization
        ++ cpg.method.fullName(".*java\\.io\\.ObjectInputStream\\.readObject:java\\.lang\\.Object\\(\\)")
        ++ cpg.method.fullName(".*java\\.io\\.ObjectInputStream\\.readUnshared:java\\.lang\\.Object\\(\\)")
        ++ cpg.method.fullName(".*java\\.beans\\.XMLDecoder\\.readObject:java\\.lang\\.Object\\(\\)")
    ).callIn.argument.l
}



def sinks = {
    println("Getting sinks...")
    (
        sink_by_arg ++
        sink_by_this
    )
}



@main def main(code: String) = {
    importCode(code)

    val result = sinks.reachableByFlows(source).map(
        _.elements.filter(e => e.astParent.isCall).map(
            e => (e.location.methodFullName, e.astParent.code)
        )
    )
    //browse(result.l)
    result.toJsonPretty |> "result_jdk.json"
}

// joern --script all_sinks.sc --params code=jars/Test2.jar


