
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:sportslink/MapaInicio.dart';
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
      resizeToAvoidBottomInset: false,
      body:
      Form(
          key: _formKey,
          child: Column(
            children: <Widget>[
              ClipRRect(
                  //borderRadius: BorderRadius.circular(90.0),
                  child:Image(
                    image: AssetImage('assets/logo.png'),
                  ),
              ),

              SizedBox(height: 30),
              TextFormField(
                validator: (input) {
                  if(input!.isEmpty){
                    return 'Correo no valido';
                  }
                },
                style: TextStyle(
                    fontSize: 20,
                    color: Color.fromRGBO(252, 252, 252, 1)),
                decoration: InputDecoration(
                    focusedBorder: UnderlineInputBorder(
                        borderSide: BorderSide(color: Color.fromRGBO(252, 252, 252, 1))),
                    labelText: 'Correo'
                ),
                onSaved: (input) => _email = input!,
              ),
              SizedBox(height: 30),
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
              new Expanded(

                  child: new Align(

                      child: new Column(
                        mainAxisAlignment: MainAxisAlignment.end,
                        children: <Widget>[
                          ElevatedButton(
                            onPressed: signIn,
                            child: Text('Iniciar sesión'),
                          ),
                          InkWell(
                              child: new Text('Registrarse'),
                              onTap: signUpRedirect
                          ),
                          SizedBox(height: 30),
                        ],
                      )))
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
        await FirebaseAuth.instance.signInWithEmailAndPassword(email: _email, password: _password);
        print('sesion iniciada');
        inicioRedirect();
      }on FirebaseAuthException catch (e){
        print(e.message);
      }
    }
  }

  void inicioRedirect(){
    Navigator.push(context,
        MaterialPageRoute(builder: (context) => MapaInicio()),);
  }
}

