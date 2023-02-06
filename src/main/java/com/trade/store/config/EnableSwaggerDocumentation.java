package com.trade.store.config;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 *
 * @author Prateek.
 */
@Retention(RUNTIME)
@Target(ElementType.TYPE)
public @interface EnableSwaggerDocumentation {

}
