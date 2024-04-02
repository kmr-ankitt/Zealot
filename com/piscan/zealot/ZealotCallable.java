package com.piscan.zealot;

import java.util.List;


interface ZealotCallable {

    int arity();
    Object call(Interpreter interpreter , List<Object> arguments);
     
}