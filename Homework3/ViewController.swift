//
//  ViewController.swift
//  GeoCalculatorApp
//
//  Created by Muna O. Gigowski on 5/14/19.
//  Copyright © 2019 Muna O. Gigowski. All rights reserved.
//

import UIKit
import CoreLocation

class ViewController: UIViewController {

    @IBOutlet weak var calculateButton: UIButton!
    @IBOutlet weak var clearButton: UIButton!
    @IBOutlet weak var distanceLabel: UILabel!
    @IBOutlet weak var longOne: UITextField!
    @IBOutlet weak var settingsButton: UIButton!
    @IBOutlet weak var bearingLabel: UILabel!
    @IBOutlet weak var latOne: UITextField!
    @IBOutlet weak var latTwo: UITextField!
    @IBOutlet weak var longTwo: UITextField!
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }


    @IBAction func onClickCalculate(_ sender: UIButton) {
        
        self.calcluateDistance()
        self.calculateBearing()
    }
    
    @IBAction func onClickClear(_ sender: UIButton) {
        latOne.text = ""
        longOne.text = ""
        latTwo.text = ""
        longTwo.text = ""
        distanceLabel.text = ""
        bearingLabel.text = ""
    }
    
    @IBAction func onClickSettings(_ sender: UIButton) {
        
        
    }
    
    
    /**
     Compute bearing in degrees between two points (in radians).
     - parameter point: The point the bearing is being computed for.
     - returns: The computed bearing in degrees.
     */
    func calculateBearing() -> Double {
        

        let x = cos(Double(latTwo.text!)!) * sin(abs(Double(longTwo.text!)! - Double(longOne.text!)!))
        let y = cos(Double(latOne.text!)!) * sin(Double(latTwo.text!)!) - sin(Double(latOne.text!)!) * cos(Double(latTwo.text!)!) * cos(abs(Double(longTwo.text!)! - Double(longOne.text!)!))
        
       // if(userSelection) {
         //   return
        //}
        
        return atan2(x,y) * 180.0 / Double.pi
    }
    
    func calcluateDistance() -> Double {
        
        let coordinateOne = CLLocation(latitude: Double(latOne.text!)!, longitude: Double(longOne.text!)!)
        let coordinateTwo = CLLocation(latitude: Double(latTwo.text!)!, longitude: Double(longTwo.text!)!)
        
        let distanceInMeters = coordinateOne.distance(from: coordinateTwo)
        
        let distanceInKilometers = distanceInMeters/1000
        
       // if(userSelection) {
        //    return distanceInMeters
       // }
        return distanceInKilometers
    }
    
}
