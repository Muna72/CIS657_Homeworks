//
//  SettingsViewController.swift
//  GeoCalculatorApp
//
//  Created by Muna O. Gigowski on 5/14/19.
//  Copyright © 2019 Muna O. Gigowski. All rights reserved.
//

import UIKit

protocol SettingsViewControllerDelegate {
    func settingsChanged(distanceUnits: String, bearingUnits: String)
}

extension UINavigationController {
    override open var preferredStatusBarStyle: UIStatusBarStyle {
        return topViewController?.preferredStatusBarStyle ?? .default
    }
}

class SettingsViewController: UIViewController, UIPickerViewDelegate, UIPickerViewDataSource {

    @IBOutlet weak var distanceLabel: UILabel!
    @IBOutlet weak var cancelButton: UIBarButtonItem!
    @IBOutlet weak var distanceUnitsLabel: UILabel!
    @IBOutlet weak var bearingUnitsLabel: UILabel!
    @IBOutlet weak var distanceUnitList: UIPickerView!
    @IBOutlet weak var bearingUnitsList: UIPickerView!
    @IBOutlet weak var saveButton: UIBarButtonItem!
    @IBOutlet weak var bearingLabel: UILabel!
    
    var distanceData: [String] = [String]()
    var bearingData:  [String] = [String]()
    var isDistance = false;
    var delegate: SettingsViewControllerDelegate?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        distanceData = ["Kilometers","Miles"]
        bearingData = ["Degrees","Mils"]
        
        // Connect data:
        self.distanceUnitList.delegate = self
        self.distanceUnitList.dataSource = self
        self.bearingUnitsList.delegate = self
        self.bearingUnitsList.dataSource = self
        
        let tapGestureRecognizer = UITapGestureRecognizer(target:self, action:      #selector(self.onUserClickForDirection))
        tapGestureRecognizer.numberOfTapsRequired = 1
    distanceUnitsLabel.addGestureRecognizer(tapGestureRecognizer)
        
        let tapTwo = UITapGestureRecognizer(target:self, action:      #selector(self.onUserClickForBearing))
        tapTwo.numberOfTapsRequired = 1
        bearingUnitsLabel.addGestureRecognizer(tapTwo)
        self.setNeedsStatusBarAppearanceUpdate()
    }
    
    override var preferredStatusBarStyle: UIStatusBarStyle {
        return .lightContent
    }
    
    @IBAction func onClickSave(_ sender: UIBarButtonItem) {
        delegate?.settingsChanged(distanceUnits: distanceUnitsLabel.text!, bearingUnits: bearingUnitsLabel.text!)
        self.dismiss(animated: true, completion: nil)
    }
    
    @IBAction func onClickCancel(_ sender: UIBarButtonItem) {
        self.dismiss(animated: true, completion: nil)
    }
    
    @objc func onUserClickForDirection(_ sender: UITapGestureRecognizer) {
        bearingUnitsList.isHidden = true;
        distanceUnitList.isHidden = false;
        isDistance = true;
    }
    
    @objc func onUserClickForBearing(_ sender: UITapGestureRecognizer) {
        distanceUnitList.isHidden = true;
        bearingUnitsList.isHidden = false;
        isDistance = false;
    }
    
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return distanceData.count
    }
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        
        if(pickerView == bearingUnitsList) {
            return bearingData[row]
        } else {
            return distanceData[row]
        }
    }
    
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        
        if(pickerView == distanceUnitList) {
            distanceUnitsLabel.text = distanceData[row]
        } else {
            bearingUnitsLabel.text = bearingData[row]
        }
    }
    
}
