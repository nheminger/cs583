unset key
set term jpeg
set output "a.jpg"
set xrange [0:605]
set yrange [0:605]
set multiplot
plot "data.dat" using ($1):($3==1 ? $2 : 1/0) lt 1 ps 0.8 pt 7
plot "data.dat" using ($1):($3==2 ? $2 : 1/0) lt 2 ps 0.8 pt 7
plot "data.dat" using ($1):($3==3 ? $2 : 1/0) lt 3 ps 0.8 pt 7
plot "data.dat" using ($1):($3==4 ? $2 : 1/0) lt 4 ps 0.8 pt 7
plot "data.dat" using ($1):($3==5 ? $2 : 1/0) lt 5 ps 0.8 pt 7
plot "data.dat" using ($1):($3==6 ? $2 : 1/0) lt 6 ps 0.8 pt 7
plot "data.dat" using ($1):($3==7 ? $2 : 1/0) lt 7 ps 0.8 pt 7
plot "data.dat" using ($1):($3==8 ? $2 : 1/0) lt 8 ps 0.8 pt 7
unset multiplot
