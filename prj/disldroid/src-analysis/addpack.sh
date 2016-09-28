for dir in `ls -d ./* | grep -v addpack.sh`; 
do 
  cd $dir
  for file in ` ls `;
  do
    echo $file
 #   echo "package javamop.${dir};" > ../tmp/$file; cat $file >> "../tmp/${file}"; 
  done
  cd ..
done
