
import 'package:firebase_auth/firebase_auth.dart';
import 'package:firebase_database/firebase_database.dart';
import 'package:flutter/material.dart';


class SignUpPage extends StatefulWidget {
  @override
  _SignUpPageState createState() => new _SignUpPageState();
}

class _SignUpPageState extends State<SignUpPage> {


  final fb = FirebaseDatabase.instance.reference().child('users/');
  final myController = TextEditingController();
  
  final GlobalKey<FormState> _formKey = GlobalKey<FormState>();
  late String _email, _password, _name, _lastName, _desc, _cellNum;

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
                    return 'Provide an email';
                  }
                },
                decoration: InputDecoration(
                    labelText: 'Email'
                ),
                onSaved: (input) => _email = input!,
              ),
              TextFormField(
                validator: (input) {
                  if(input!.length < 6){
                    return 'Longer password please';
                  }
                },
                decoration: InputDecoration(
                    labelText: 'Password'
                ),
                onSaved: (input) => _password = input!,
                obscureText: true,
              ),
              TextFormField(
                validator: (input) {
                  if(input!.isEmpty){
                    return 'Ingresa tu nombre';
                  }
                },
                decoration: InputDecoration(
                    labelText: 'Nombre'
                ),
                onSaved: (input) => _name = input!,
                
              ),
              TextFormField(
                validator: (input) {
                  if(input!.isEmpty){
                    return 'Ingresa tu appelido';
                  }
                },
                decoration: InputDecoration(
                    labelText: 'Apellido'
                ),
                onSaved: (input) => _lastName = input!,
              
              ),
              TextFormField(
                validator: (input) {
                  if(input!.isEmpty){
                    return 'Ingresa tu número de celular';
                  }
                },
                decoration: InputDecoration(
                    labelText: 'Celular'
                ),
                onSaved: (input) => _cellNum = input!,
              
              ),
              

              ElevatedButton(
                onPressed: signUp,
                child: Text('Continuar'),
              ),
            ],
          )
      ),
    );
  }

  void signUp() async {
    _desc = 'usuario de flutter';
    final ref = fb.reference();
    if(_formKey.currentState!.validate()){
      _formKey.currentState!.save();
      try{
        User? user = (await FirebaseAuth.instance.createUserWithEmailAndPassword(email: _email, password: _password)).user;
        UserCredential userCredential;
        print('Usuario registrado');

        Map<String,String> contact = {
          'correo': _email,
          'descripcion': _desc,
          'id': user!.uid,
          'lastName': _lastName,
          'name': _name,
          'numeroCelular': _cellNum,
        };

        ref.child(user.uid).set(contact).then((value) {
          Navigator.pop(context);
        });
        print('Usuario guardado en firebase realtime database');
      }on FirebaseAuthException catch (e){
        print(e.message);
      }
    }
  }

  @override
  void dispose() {
    // Clean up the controller when the widget is disposed.
    myController.dispose();
    super.dispose();
  }

}

