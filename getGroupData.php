<?php
define('HOST','mysql.hostinger.co.uk');
define('USER','u997523806_user');
define('PASS','pupp33');
define('DB','u997523806_db');
$con = mysqli_connect(HOST,USER,PASS,DB);

 $name = $_POST['GROUP_NAME'];
 $t1 = 'a';


 #$sql = "SELECT GROUP_ID, SHOPPINGLIST, CALENDAR, MONEY, TODO, OWNER_ID FROM FlatGroups WHERE GROUP_NAME='".$t1."'";

$sql = "SELECT GROUP_ID, SHOPPINGLIST, CALENDAR, MONEY, TODO, OWNER_ID FROM FlatGroups WHERE GROUP_NAME='".$name."'";
$result = $con->query($sql);

if ($result->num_rows > 0) {
  // output data of each row
  while($row = $result->fetch_assoc()) {
      echo "successGG," . $row["GROUP_ID"].
  "," . $row["SHOPPINGLIST"].
  "," . $row["CALENDAR"].
  "," . $row["TODO"].
  "," . $row["OWNER_ID"].
  "," . $row["MONEY"];
  }
} else {
  echo "0 results";
}
mysqli_close($con);
?>
