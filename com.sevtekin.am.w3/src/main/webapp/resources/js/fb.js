(function () {
  
	console.log('HERE');
	alert('yay');
	var config = {
    apiKey: "AIzaSyAweSkT-Phorxt_10a0FdiZ8rjWAQUFKuE",
    authDomain: "assetmanager-171616.firebaseapp.com",
    databaseURL: "https://assetmanager-171616.firebaseio.com",
    projectId: "assetmanager-171616",
    storageBucket: "assetmanager-171616.appspot.com",
    messagingSenderId: "232262905979"
  };
  firebase.initializeApp(config);
  
  const txtUser = document.getElementById('user');
  const txtPass = document.getElementById('pass');
  
  btnLogin.addEventListener('click', e => {
	  
	  
	  const user = txtUser.value;
	  const pass = txtPass.value;
	  const auth = firebase.auth();
	  firebase.auth().setPersistence(firebase.auth.Auth.Persistence.SESSION)
	  .then(function() {
	    // Existing and future Auth states are now persisted in the current
	    // session only. Closing the window would clear any existing state even
	    // if a user forgets to sign out.
	    // ...
	    // New sign-in will be persisted with session persistence.
	    return firebase.auth().signInWithEmailAndPassword(user, pass);
	  })
	  .catch(function(error) {
	    // Handle Errors here.
	    var errorCode = error.code;
	    var errorMessage = error.message;
	    console.log(errorCode + ': ' + errorMessage);
	  });
	  
  });

  }());
