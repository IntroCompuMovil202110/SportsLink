
import 'package:firebase_auth/firebase_auth.dart';
import 'package:firebase_database/firebase_database.dart';
import 'package:flutter/material.dart';
import 'package:sportslink/MapaInicio.dart';


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
    return Scaffold(
        body: Container(
          color: Color.fromRGBO(36, 43, 47, 1),
          padding: const EdgeInsets.symmetric(horizontal: 43.0),
          child: Form(
            key: _formKey,
            child: Container(
              alignment: Alignment.center,
              child: SingleChildScrollView(    // new line
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: <Widget>[
                    ClipRRect(
                      borderRadius: BorderRadius.circular(90.0),
                      child:Image(
                        image: AssetImage('assets/knights.png'),
                        width: 200,
                        height: 200,
                      ),
                    ),
                    _buildCorreo(),
                    _buildContra(),
                    _buildFirstName(),
                    _buildLastName(),
                    _buildCelular(),
                    SizedBox(height: 40),
                    ElevatedButton(
                      onPressed: signUp,
                      child: Text('Continuar'),
                    ),
                    SizedBox(height: 10),
                    InkWell(
                        child: new Text('Iniciar sesión'),
                        onTap: volverInicioSesion
                    ),
                  ],
                ),
              ),
            ),
          ),
        ));
  }

  /*@override
  Widget build(BuildContext context) {
    
    return new Scaffold(
      appBar: new AppBar(),
      resizeToAvoidBottomInset: false,
        body: Container(
          color: Color.fromRGBO(36, 43, 47, 1),
          padding: const EdgeInsets.symmetric(horizontal: 43.0),
          child: Form(
            key: _formKey,
            child: Container(
              alignment: Alignment.center,
              child: SingleChildScrollView(    // new line
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: <Widget>[
                    _buildFirstName(),
                    _buildLastName(),
                    _buildLastName(),
                    _buildLastName(),
                    _buildLastName(),
                  ],
                ),
              ),
            ),
          ),
        )
        /*body:
          Container(
              color: Color.fromRGBO(36, 43, 47, 1),
              padding: const EdgeInsets.symmetric(horizontal: 43.0),
              child: Form(
                  key: _formKey,
                  child: Container(
                      alignment: Alignment.center,
                      child: SingleChildScrollView(
                          child: Column(
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: <Widget>[
                              ClipRRect(
                                borderRadius: BorderRadius.circular(90.0),
                                child:Image(
                                  image: AssetImage('assets/knights.png'),
                                  width: 200,
                                  height: 200,
                                ),
                              ),
                              TextFormField(
                                validator: (input) {
                                  if(input!.isEmpty){
                                    return 'Proporciona un correo';
                                  }
                                },
                                onSaved: (input) => _email = input!,
                                style: TextStyle(fontSize: 20),
                                decoration: InputDecoration(
                                  focusedBorder: UnderlineInputBorder(
                                      borderSide: BorderSide(color: Color.fromRGBO(252, 252, 252, 1))),
                                  labelText: "Correo",
                                  enabledBorder: UnderlineInputBorder(
                                      borderSide: BorderSide(color: Color.fromRGBO(151, 151, 151, 1))),
                                  labelStyle: TextStyle(color: Color.fromRGBO(252, 252, 252, 1)),

                                ),

                              ),
                              TextFormField(
                                validator: (input) {
                                  if(input!.length < 6){
                                    return 'Debe tener al menos 6 caractéres';
                                  }
                                },
                                style: TextStyle(fontSize: 20),
                                decoration: InputDecoration(
                                  focusedBorder: UnderlineInputBorder(
                                      borderSide: BorderSide(color: Color.fromRGBO(252, 252, 252, 1))),
                                  labelText: "Contraseña",
                                  enabledBorder: UnderlineInputBorder(
                                      borderSide: BorderSide(color: Color.fromRGBO(151, 151, 151, 1))),
                                  labelStyle: TextStyle(color: Color.fromRGBO(252, 252, 252, 1)),

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
                                onSaved: (input) => _name = input!,
                                style: TextStyle(fontSize: 20),
                                decoration: InputDecoration(
                                  focusedBorder: UnderlineInputBorder(
                                      borderSide: BorderSide(color: Color.fromRGBO(252, 252, 252, 1))),
                                  labelText: "Nombre",
                                  enabledBorder: UnderlineInputBorder(
                                      borderSide: BorderSide(color: Color.fromRGBO(151, 151, 151, 1))),
                                  labelStyle: TextStyle(color: Color.fromRGBO(252, 252, 252, 1)),

                                ),


                              ),
                              TextFormField(
                                validator: (input) {
                                  if(input!.isEmpty){
                                    return 'Ingresa tu apellido';
                                  }
                                },
                                style: TextStyle(fontSize: 20),
                                decoration: InputDecoration(
                                  focusedBorder: UnderlineInputBorder(
                                      borderSide: BorderSide(color: Color.fromRGBO(252, 252, 252, 1))),
                                  labelText: "Apellido",
                                  enabledBorder: UnderlineInputBorder(
                                      borderSide: BorderSide(color: Color.fromRGBO(151, 151, 151, 1))),
                                  labelStyle: TextStyle(color: Color.fromRGBO(252, 252, 252, 1)),

                                ),
                                onSaved: (input) => _lastName = input!,

                              ),
                              TextFormField(
                                validator: (input) {
                                  if(input!.isEmpty){
                                    return 'Ingresa tu número de celular';
                                  }
                                },
                                style: TextStyle(fontSize: 20),
                                decoration: InputDecoration(
                                  focusedBorder: UnderlineInputBorder(
                                      borderSide: BorderSide(color: Color.fromRGBO(252, 252, 252, 1))),
                                  labelText: "Celular",
                                  enabledBorder: UnderlineInputBorder(
                                      borderSide: BorderSide(color: Color.fromRGBO(151, 151, 151, 1))),
                                  labelStyle: TextStyle(color: Color.fromRGBO(252, 252, 252, 1)),

                                ),
                                onSaved: (input) => _cellNum = input!,

                              ),
                              new Expanded(
                                  child: new Align(
                                      child: new Column(
                                        mainAxisAlignment: MainAxisAlignment.end,
                                        children: <Widget>[
                                          ElevatedButton(
                                            onPressed: signUp,
                                            child: Text('Continuar'),
                                          ),
                                          InkWell(
                                              child: new Text('Iniciar sesión'),
                                              onTap: volverInicioSesion
                                          ),
                                          SizedBox(height: 30),
                                        ],
                                      )))
                            ],
                          )
                      )
                  )

              ),
          )*/

    );
  }*/

  void volverInicioSesion() {
    Navigator.pop(context);
  }

  void signUp() async {
    print(_email);
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
        inicioRedirect();
      }on FirebaseAuthException catch (e){
        print(e.message);
      }
    }
  }

  InputDecoration _buildInputDecoration(String hint) {
    return InputDecoration(
        focusedBorder: UnderlineInputBorder(
            borderSide: BorderSide(color: Color.fromRGBO(252, 252, 252, 1))),

        enabledBorder: UnderlineInputBorder(
            borderSide: BorderSide(color: Color.fromRGBO(151, 151, 151, 1))),
        hintStyle: TextStyle(color: Color.fromRGBO(252, 252, 252, 1)),
        errorStyle: TextStyle(color: Color.fromRGBO(248, 218, 87, 1)),
        errorBorder: UnderlineInputBorder(
            borderSide: BorderSide(color:  Color.fromRGBO(248, 218, 87, 1))),
        focusedErrorBorder: UnderlineInputBorder(
            borderSide: BorderSide(color:  Color.fromRGBO(248, 218, 87, 1))));
  }

  @override
  void dispose() {
    // Clean up the controller when the widget is disposed.
    myController.dispose();
    super.dispose();
  }

  _buildCorreo(){
    return TextFormField(
      validator: (input) {
        if(input!.isEmpty){
          return 'Proporciona un correo';
        }
      },
      onSaved: (input) => _email = input!,
      style: TextStyle(fontSize: 20),
      decoration: InputDecoration(
        focusedBorder: UnderlineInputBorder(
            borderSide: BorderSide(color: Color.fromRGBO(252, 252, 252, 1))),
        labelText: "Correo",
        enabledBorder: UnderlineInputBorder(
            borderSide: BorderSide(color: Color.fromRGBO(151, 151, 151, 1))),
        labelStyle: TextStyle(color: Color.fromRGBO(252, 252, 252, 1)),

      ),

    );
  }

  _buildContra(){
    return TextFormField(
        validator: (input) {
      if(input!.length < 6){
        return 'Debe tener al menos 6 caractéres';
      }
    },
        onSaved: (input) => _password = input!,
    style: TextStyle(fontSize: 20),
    decoration: InputDecoration(
    focusedBorder: UnderlineInputBorder(
    borderSide: BorderSide(color: Color.fromRGBO(252, 252, 252, 1))),
    labelText: "Contraseña",
    enabledBorder: UnderlineInputBorder(
    borderSide: BorderSide(color: Color.fromRGBO(151, 151, 151, 1))),
    labelStyle: TextStyle(color: Color.fromRGBO(252, 252, 252, 1)),

    )
    );
  }


  _buildCelular(){
    return TextFormField(
      validator: (input) {
        if(input!.isEmpty){
          return 'Ingresa tu número de celular';
        }
      },
      onSaved: (input) => _cellNum = input!,
      style: TextStyle(fontSize: 20),
      decoration: InputDecoration(
        focusedBorder: UnderlineInputBorder(
            borderSide: BorderSide(color: Color.fromRGBO(252, 252, 252, 1))),
        labelText: "Celular",
        enabledBorder: UnderlineInputBorder(
            borderSide: BorderSide(color: Color.fromRGBO(151, 151, 151, 1))),
        labelStyle: TextStyle(color: Color.fromRGBO(252, 252, 252, 1)),

      ),


    );
  }

  _buildLastName() {
    return TextFormField(
      validator: (input) {
        if(input!.isEmpty){
          return 'Ingresa tu apellido';
        }
      },
      style: TextStyle(fontSize: 20),
      decoration: InputDecoration(
        focusedBorder: UnderlineInputBorder(
            borderSide: BorderSide(color: Color.fromRGBO(252, 252, 252, 1))),
        labelText: "Apellido",
        enabledBorder: UnderlineInputBorder(
            borderSide: BorderSide(color: Color.fromRGBO(151, 151, 151, 1))),
        labelStyle: TextStyle(color: Color.fromRGBO(252, 252, 252, 1)),

      ),
      onSaved: (input) => _lastName = input!,

    );
  }

  _buildFirstName() {
    return TextFormField(
      validator: (input) {
        if(input!.isEmpty){
          return 'Proporciona un correo';
        }
      },
      onSaved: (input) => _name = input!,
      style: TextStyle(fontSize: 20),
      decoration: InputDecoration(
        focusedBorder: UnderlineInputBorder(
            borderSide: BorderSide(color: Color.fromRGBO(252, 252, 252, 1))),
        labelText: "Correo",
        enabledBorder: UnderlineInputBorder(
            borderSide: BorderSide(color: Color.fromRGBO(151, 151, 151, 1))),
        labelStyle: TextStyle(color: Color.fromRGBO(252, 252, 252, 1)),

      ),

    );
  }

  void inicioRedirect(){
    Navigator.push(context,
      MaterialPageRoute(builder: (context) => MapaInicio()),);
  }

}



