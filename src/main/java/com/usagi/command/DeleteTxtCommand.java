package com.usagi.command;

import com.usagi.component.VectorStorage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;


@Slf4j
@AllArgsConstructor
@ShellComponent
public class DeleteTxtCommand {

    final VectorStorage vectorStorage;

    @ShellMethod(value = "delete stored txt data")
    public String delete(String doc){
        log.info("start delete doc.");
        // store vector
        String collection= vectorStorage.getCollectionName();
        vectorStorage.deleteByDocId(collection, doc);
        log.info("finished");
        return "delete docId:{}" + doc;
    }
}
