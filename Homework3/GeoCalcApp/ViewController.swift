//
//  ViewController.swift
//  GeoCalculatorApp
//
//  Created by Muna O. Gigowski on 5/14/19.
//  Copyright © 2019 Muna O. Gigowski. All rights reserved.
//

import UIKit
import CoreLocation

class ViewController: UIViewController, SettingsViewControllerDelegate {
    
    @IBOutlet weak var calculateButton: UIButton!
    @IBOutlet weak var clearButton: UIButton!
    @IBOutlet weak var distanceLabel: UILabel!
    
    @IBOutlet weak var longOne: UITextField!
    @IBOutlet weak var latOne: UITextField!
    @IBOutlet weak var settingsButton: UIButton!
    @IBOutlet weak var bearingLabel: UILabel!
    
    @IBOutlet weak var latTwo: UITextField!
    @IBOutlet weak var longTwo: UITextField!
    override func viewDidLoad() {
        super.viewDidLoad()
        latOne.placeholder = "Enter latitude for P1"
        latTwo.placeholder = "Enter Latitude for P2"
        longOne.placeholder = "Enter Longitude for P1"
        longTwo.placeholder = "Enter Longitude for P2"
        // Do any additional setup after loading the view.
        self.hideKeyboardWhenTappedAround()
    }


    @IBAction func onClickCalculate(_ sender: UIButton) {
        
        distanceLabel.text = String(self.calcluateDistance())
        bearingLabel.text = String(self.calculateBearing())
    }
    
    @IBAction func onClickClear(_ sender: UIButton) {
        latOne.text = ""
        longOne.text = ""
        latTwo.text = ""
        longTwo.text = ""
        distanceLabel.text = ""
        bearingLabel.text = ""
    }
    
   // override func touchesBegan : withEvent() {
       // self.endEditing();
   // }
    
    /**
     Compute bearing in degrees between two points (in radians).
     - parameter point: The point the bearing is being computed for.
     - returns: The computed bearing in degrees.
     */
    func calculateBearing() -> String {
        

        let x = cos(Double(latTwo.text!)!) * sin(abs(Double(longTwo.text!)! - Double(longOne.text!)!))
        let y = cos(Double(latOne.text!)!) * sin(Double(latTwo.text!)!) - sin(Double(latOne.text!)!) * cos(Double(latTwo.text!)!) * cos(abs(Double(longTwo.text!)! - Double(longOne.text!)!))
        
        let bearingInDegrees = atan2(x,y) * 180.0 / Double.pi
        let bearingInMils = bearingInDegrees * 17.777777777
        
        if(bearingLabel.text == "Mils") {
            return String(format: "%.2f",bearingInMils) + " Mils"
        }
        
        return String(format: "%.2f",bearingInDegrees) + " Degrees"
    }
    
    func calcluateDistance() -> String {
        
        let coordinateOne = CLLocation(latitude: Double(latOne.text!)!, longitude: Double(longOne.text!)!)
        let coordinateTwo = CLLocation(latitude: Double(latTwo.text!)!, longitude: Double(longTwo.text!)!)
        
        let distanceInMeters = coordinateOne.distance(from: coordinateTwo)
        
        let distanceInKilometers = distanceInMeters/1000
        let distanceInMiles = distanceInKilometers * 0.621371
        
        if(distanceLabel.text == "Miles") {
            return String(format: "%.2f",distanceInMiles) + " Miles"
        }
        return String(format: "%.2f",distanceInKilometers) + " Kilometers"
    }
    
    func settingsChanged(distanceUnits: String, bearingUnits: String) {
        distanceLabel.text = distanceUnits
        bearingLabel.text = bearingUnits
        onClickCalculate(calculateButton)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "homeToSettings" {
            if let destNav = segue.destination as? UINavigationController {
                if let dest = destNav.children[0] as?
                    SettingsViewController {
                    dest.delegate = self
                }
            }
            
        }
        
        
    }
    
    
}

extension UIViewController {
    func hideKeyboardWhenTappedAround() {
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(UIViewController.dismissKeyboard))
        tap.cancelsTouchesInView = false
        view.addGestureRecognizer(tap)
    }
    
    @objc func dismissKeyboard() {
        view.endEditing(true)
    }
}


