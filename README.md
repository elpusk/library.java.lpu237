# library.java.lpu237
+ Java library for lpu237 of Android.
+ current version 1.5.0
+ folders
  * liblpu237 - library module folder
  * test_liblpu237 - test app of library module folder 
  * app - Don't use it. Only for developer.
+ firmware condition
  * callisto version 3.24(lpu23x_00034.rom) or higher. [See](https://github.com/elpusk/public.lpu237.firmware/tree/main/lpu23x)
  * ganymede version 5.23(lpu23x_00034.rom) or higher. [See](https://github.com/elpusk/public.lpu237.firmware/tree/main/lpu23x)
  * himalia version 2.4(lpu23x_00034.rom) or higher. [See](https://github.com/elpusk/public.lpu237.firmware/tree/main/lpu23x)

## [liblpu237](liblpu237) 
  + This is the library module folder.
  + library type is aar.
  + the library is generated  in "liblpu237\build\outputs\aar".

## Library Description
+ JavaDoc type
+ [See](https://htmlpreview.github.io/?https://github.com/elpusk/library.java.lpu237/blob/main/javadoc/index.html)
+ Example source [here](https://github.com/elpusk/example.adr.lib)

## how to build.(AAR)
+ In build variants, Set Active Build Variant to release
+ buid(CTL+F9).

## how to generate the spec of liblpu237.(javaDoc)
+ run "gradlew :liblpu237:generateJavadoc"
+ Docments is is generated  in "liblpu237\build\docs\javadoc".

  

