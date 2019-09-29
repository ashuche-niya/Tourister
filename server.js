const express = require('express');
const bodyParser =require('body-parser');
var knex = require('knex');
const cors = require('cors');
// const db=knex({
//   client: 'pg',
//   connection: {
//     host : '127.0.0.1',
//     user : 'postgres',
//     password : 'super',
//     database : 'newdb'
//   }
// });
const Pusher = require('pusher');

const pusher = new Pusher({
  appId: '870544',
  key: 'c70e4222e6ed545b2782',
  secret: '0025b898faa02beec0e7',
  cluster: 'ap2',
  encrypted: true
});
const db = knex({
  client: 'pg',
  connection: {
    connectionString: process.env.DATABASE_URL,
    ssl: true
  }
});



// const namei='ishan'
// db.select('*').from('users').where('username','=',namei).then(data=>{
//   console.log(data);
// })

// db('users').insert(
// {username: 'ishan',
// email: 'ish@gmail.com',
// phone:'455'
// }).then();
const app = express();
app.use(bodyParser.json());
app.use(cors());

app.get('/',(req,res)=> {
    res.send("this is working");
});

app.post('/signin',(req,res)=> {
    //const { name,mail,phoneno } = req.body;
    //console.log(req.body.username);
    db('users').insert(
    {username: req.body.username,
    email:req.body.email,
    phone:req.body.phone
    }).then();
    res.json("hellooo!");
});

app.post('/createteam',(req,res)=> {
  db('allteamname').where('teamname', '=', req.body.teamname)
  .then(data =>{
    if (data.length===0){
      db('allteamname').insert(
      {teamname: req.body.teamname
      }).then();

        db.schema.createTable(req.body.teamname, function (table) {
        table.string('username');
        table.string('location');
        table.string('phone');
        table.string('paid');

      }).then(data=>{
            if (data.command=='CREATE') {
              db(req.body.teamname).insert(
              {username: req.body.username,
              location:req.body.location,
              phone:req.body.phone,
              paid:"0"
              }).then();
            }
      });

      db.schema.createTable(req.body.chatname,function(table){
        table.string('chathead');
    }).then();
      res.json("team created");
    }
    else {
      res.json("team already exist");
    }
  });
});



app.post('/jointeam',(req,res)=> {
  db('allteamname').where('teamname', '=', req.body.teamname)
  .then(data =>{
    if (data.length!=0){
      db(req.body.teamname).insert(
      {username: req.body.username,
      location:req.body.location,
      phone:req.body.phone,
      paid:"0"
    }).then(data=>{
      db.select('username').from(req.body.teamname).then(data=>{
        const alluserlist={userlist:data};
        // console.log(alluserlist);
        pusher.trigger('jointeamch', 'jointeamevent', JSON.stringify(alluserlist));
      });
    });


      res.json("joined successfully");
    }
    else {
      res.json("enter valid team name");
    }
  })
});

app.post('/addpayment',(req,res)=>{
  var last_pay;
  db(req.body.teamname).where('username', '=', req.body.username)
  .then(data =>{
    if(data.length!=0){
      last_pay=parseInt(data[0].paid);
    }
    else{
      res.json("No such team");
    }
  })

  // db(req.body.teamname).where('username', '=', req.body.username)
  // .update({paid:(parseInt(req.body.money)+last_pay).toString()}).then(data=>{
  //   db.select('paid').from(req.body.teamname).then(data=>{
  //     //res.json(data)
  //     console.log(data);
  //     const Allpayments={payments:data};
  //     pusher.trigger('teamMember', 'updatePayments', JSON.stringify(Allpayments))
  //   })
  // })
res.json("payments added");
});
app.post('/updateAllTeamLocation',(req,res)=>{
  var len=req.body.teamlist.length;
  for(i=0;i<len;i++){
    db(req.body.teamlist[i].teamname).where('username', '=', req.body.username)
    .update({location:req.body.location}).then(data=>{
      //res.json(data)
      const userLocation={location:data};
      pusher.trigger('teamMember', 'updateLocationAll', JSON.stringify(userLocation));
    })
  }
})
// app.post('/signin',(req,res)=> {
//     //const { name,mail,phoneno } = req.body;
//     //console.log(req.body.username);
//     db.select('*').from('users').where('email','=',req.body.email).then(data=>{
//       if (data.length==0) {
//         db('users').insert(
//         {username: req.body.username,
//         email:req.body.email,
//         phone:req.body.phone
//       }).then();
//       }
//     })
//     res.json("response from the signin end");
// });
//
// app.post('/createteam',(req,res)=> {
//   db('allteamname').where('teamname', '=', req.body.teamname)
//   .then(data =>{
//     if (data.length===0){
//       db('allteamname').insert(
//       {teamname: req.body.teamname
//       }).then();
//
//         db.schema.createTable(req.body.teamname, function (table) {
//         table.string('username');
//         table.string('location');
//         table.string('phone');
//         table.string('paid');
//       }).then(data=>{
//             if (data.command=='CREATE') {
//               db(req.body.teamname).insert(
//               {username: req.body.username,
//               location:req.body.location,
//               phone:req.body.phone
//               paid:'0'
//               }).then();
//             }
//       });
//       res.json("team created");
//     }
//     else {
//       res.json("team already exist");
//     }
//   });
// });
//
//
//
//
// app.post('/jointeam',(req,res)=> {
//   db('allteamname').where('teamname', '=', req.body.teamname)
//   .then(data =>{
//     if (data.length!=0){
//       db(req.body.teamname).insert(
//       {username: req.body.username,
//       location:req.body.location,
//       phone:req.body.phone
//     }).then(data=>{
//       db.select('username').from(req.body.teamname).then(data=>{
//         const alluserlist={userlist:data};
//         pusher.trigger('jointeamch', 'jointeamevent',JSON.stringify(alluserlist));
//       })
//     });
//       res.json("joined successfully");
//     }
//     else {
//       res.json("enter valid team name");
//     }
//   })
// });
app.listen(process.env.PORT || 3000, () => {
	console.log(`app is running on port 3000 ${process.env.PORT}`);
})
