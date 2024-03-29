// 
// Decompiled by Procyon v0.5.36
// 

package io.fazal.cloudpass.utils.pair;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
public @interface NamedArg {
    String value();
    
    String defaultValue() default "";
}
