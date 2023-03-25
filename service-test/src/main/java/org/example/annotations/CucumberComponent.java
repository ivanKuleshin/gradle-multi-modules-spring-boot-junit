package org.example.annotations;

import groovy.transform.AnnotationCollector;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("cucumber-glue")
@AnnotationCollector
public @interface CucumberComponent {
}
