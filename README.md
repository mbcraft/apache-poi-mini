
This library is a modification of the original 
Apache POI from Apache Software Foundation. (i think version 5.0.0)

Basically the part for reading the sheet files in xml was
completely removed due to a conflict with some android
libraries. This enabled this library to run on a tablet
,populate cell with values formulas and calculate them. 
All the "unnecessary"? libraries were removed in order
to make it as lighter as possible.

I added the implementation of a function (NUMBERVALUE)
and a class that enabled the translation of the formulas
written in different languages. Actually it supports
the Italian translation only (and English formulas too).

The main interface is a facade 
(org.apache.poimini.ExcelManager) that contains all the methods to 
operate.

There is also an example class (org.apache.poimini.example.Main)
with some example usages. To see it in action just uncomment 
the methods and run it.

-Marco Bagnaresi (info@mbcraft.it)