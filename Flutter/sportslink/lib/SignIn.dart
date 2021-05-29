
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:sportslink/SignUp.dart';

import 'AuthenticationService.dart';

class LoginPage extends StatefulWidget {
  @override
  _LoginPageState createState() => new _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {

  final GlobalKey<FormState> _formKey = GlobalKey<FormState>();
  late String _email, _password;

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(),
      body: Form(
          key: _formKey,
          child: Column(
            children: <Widget>[
              TextFormField(
                validator: (input) {
                  if(input!.isEmpty){
                    return 'Correo no valido';
                  }
                },
                decoration: InputDecoration(
                    labelText: 'Correo'
                ),
                onSaved: (input) => _email = input!,
              ),
              TextFormField(
                validator: (input) {
                  if(input!.length < 6){
                    return 'La contraseña debe tener al menos 6 caractéres';
                  }
                },
                decoration: InputDecoration(
                    labelText: 'Contraseña'
                ),
                onSaved: (input) => _password = input!,
                obscureText: true,
              ),
              ElevatedButton(
                onPressed: signIn,
                child: Text('Iniciar sesión'),
              ),
              InkWell(
                child: new Text('Registrarse'),
                onTap: signUpRedirect
              ),
            ],
          )
      ),
    );
  }

  void signUpRedirect(){
    Navigator.push(
      context,
      MaterialPageRoute(builder: (context) => SignUpPage()),
    );
  }

  void signIn() async {
    if(_formKey.currentState!.validate()){
      _formKey.currentState!.save();
      try{
        User user = (await FirebaseAuth.instance.signInWithEmailAndPassword(email: _email, password: _password)) as User;
        print('sesion iniciada');
      }on FirebaseAuthException catch (e){
        print(e.message);
      }
    }
  }
}

