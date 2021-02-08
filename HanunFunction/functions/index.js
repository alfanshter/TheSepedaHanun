'use-strict'
const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sendNotification = functions.firestore.document("Users/{user_id}/Notifications/{notification_id}").onWrite((change,context)=>{
const user_id = context.params.user_id;
const notification_id = context.params.notification_id;

return admin.firestore().collection("Users").doc(user_id).collection("Notifications").doc(notification_id).get().then(queryResult =>{
  
    const from_user_id = queryResult.data().from;
    const from_message = queryResult.data().message;
    const suhu = queryResult.data().suhu;
    const detak_jantung = queryResult.data().detak_jantung;
    const diagnosis = queryResult.data().diagnosis;
  
    const from_data = admin.firestore().collection("Users").doc(from_user_id).get();
    const to_data = admin.firestore().collection("Users").doc(user_id).get();
  
    return Promise.all([from_data, to_data]).then(result =>{
        const token_id = result[1].data().token_id;
        const payload = {
            notification:{
                title :"Notification From : ",
                body : from_message,
                icon : "default",
                click_action : "dipdip.android.dip.com.hanun.TARGETNOTIFICATION"
            },
            data: {
                message : from_message,
                from_user_id : from_user_id,
                suhu : suhu,
                detak_jantung : detak_jantung,
                diagnosis : diagnosis
                
            }
        };

        return admin.messaging().sendToDevice(token_id,payload).then(result =>{
          return  console.log("Notifications Send");
        });
    });
});


});

