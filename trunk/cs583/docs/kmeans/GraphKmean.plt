unset key
set xrange [0:305]
set yrange [0:305]
set multiplot
plot "data.dat" using ($1):($3==1 ? $2 : 1/0) lt 1 ps 0.25 pt 7
plot "data.dat" using ($1):($3==2 ? $2 : 1/0) lt 2 ps 0.25 pt 7
plot "data.dat" using ($1):($3==3 ? $2 : 1/0) lt 3 ps 0.25 pt 7
plot "data.dat" using ($1):($3==4 ? $2 : 1/0) lt 4 ps 0.25 pt 7
unset multiplot
