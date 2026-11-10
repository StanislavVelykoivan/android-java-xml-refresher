package com.velykoivanstanislav.animals.di;

import javax.inject.Qualifier;

@Qualifier
public @interface TypeOfContext {
    public static final String CONTEXT_APP = "application_context";
    public static final String CONTEXT_ACTIVITY = "activity_context";

    String value() default "";
}
