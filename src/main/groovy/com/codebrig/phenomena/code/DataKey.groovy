package com.codebrig.phenomena.code

/**
 * todo: description
 *
 * @version 0.1
 * @since 0.1
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
abstract class DataKey<T> {

    @Override
    int hashCode() {
        return getClass().hashCode()
    }

    @Override
    boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass()
    }

}
