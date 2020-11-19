const functions = require('firebase-functions');

const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);



exports.notification = functions.firestore.document('/chatAdmin/{chatId}/messages/{messageId}').onCreate(
  async(snapshot) =>{

    const text = snapshot.data().message;
    const senderUid = snapshot.data().sender;
    const rUid = snapshot.data().receiver;
    console.log('new message is:  ', text);
    console.log('uid is:  ', senderUid);
    console.log('r Uid is:  ', rUid);
    const payload = {
      notification: {
        title: `Админ`,
        body: text.length <= 100 ? text : text.substring(0, 97) + '...',
        icon: ('/profilePhotos/'+ senderUid + '.jpg') || 'default'
      }
    };

    var token;
    const tokenDoc = admin.firestore().collection('fcmTokens').doc(rUid).get().then(async (sn) =>{
      token = sn.data().token;
      console.log('token is:  ', token);
      await admin.messaging().sendToDevice(token, payload);
      // await cleanupTokens(response, tokenn, senderUid);

      console.log('notification has been sent and token: ', token);

    }).catch(reason => {
        res.send(reason)
    })
});
