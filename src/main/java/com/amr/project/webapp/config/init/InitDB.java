package com.amr.project.webapp.config.init;

import com.amr.project.service.impl.TestDataEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnMissingClass({"org.springframework.boot.test.context.SpringBootTest"})
public class InitDB implements CommandLineRunner {

   @Autowired
   private TestDataEntityService testDataEntityService;

   @Override
   public void run(String... args) throws Exception {
      testDataEntityService.createEntity();
   }
}
