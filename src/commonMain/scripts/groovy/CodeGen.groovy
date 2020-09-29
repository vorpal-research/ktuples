import groovy.io.FileType
import groovy.text.GStringTemplateEngine

def binding = [number: Integer.parseInt(depth)]
def engine = new GStringTemplateEngine()

def newFiles = []
def srcDir = new File("${project.resources[0].directory}")
def dstDir = new File(new File("${project.build.directory}"), "generated-sources")
dstDir.mkdirs()

srcDir.eachFileRecurse(FileType.FILES) { file ->
    if (file.name.endsWith(".template")) {
        println "Processing template " + file

        def rel = srcDir.toURI().relativize(file.toURI())
        def newFile = new File(dstDir.toURI().resolve(rel))

        newFile = new File(newFile.parentFile, newFile.name.replace(".template", ""))
        newFile.parentFile.mkdirs()
        newFile.createNewFile()
        newFiles.add([file, newFile])
    }
}

newFiles.each { file ->
    List.metaClass.map = { lambda -> delegate.map(lambda) }
    List.metaClass.repeat = { lambda -> delegate.collect(lambda).join(', ') }
    Integer.metaClass.join = { lambda -> (0..<delegate).repeat(lambda) }

    def template = engine.createTemplate(file[0]).make(binding)

    println "Writing file " + file[1]

    file[1].withWriter { writer -> template.writeTo(writer) }
}
