// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.objects.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface Function {
    String name() default "";
    
    int attributes() default 0;
    
    int arity() default -2;
    
    Where where() default Where.PROTOTYPE;
}
