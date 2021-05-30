
import 'package:flutter/material.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';


class MapaInicio extends StatefulWidget {
    _MapaInicioState createState() => _MapaInicioState();
  }

class _MapaInicioState extends State<MapaInicio>{
  late GoogleMapController mapController;

  final LatLng _center = const LatLng(4.62801113246097, -74.06517904884986);

  void _onMapCreated(GoogleMapController controller) {
    mapController = controller;
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: Text('SportsLink'),
          backgroundColor: Colors.indigo[800],
        ),
        body: GoogleMap(
          onMapCreated: _onMapCreated,
          initialCameraPosition: CameraPosition(
            target: _center,
            zoom: 11.0,
          ),
        ),
      ),
    );

  }
}




