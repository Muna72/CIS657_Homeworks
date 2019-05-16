//
//  SettingsViewController.swift
//  GeoCalculatorApp
//
//  Created by Muna O. Gigowski on 5/14/19.
//  Copyright © 2019 Muna O. Gigowski. All rights reserved.
//

import UIKit

class SettingsViewController: UIViewController {

    @IBOutlet weak var distanceUnitsLabel: UILabel!
    @IBOutlet weak var distanceLabel: UILabel!
    @IBOutlet weak var cancelButton: UIBarButtonItem!
    @IBOutlet weak var saveButton: UIBarButtonItem!
    @IBOutlet weak var bearingLabel: UILabel!
    @IBOutlet weak var bearingUnitsLabel: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
    }
    
    @IBAction func onClickSave(_ sender: UIBarButtonItem) {
        
    }
    
    
    @IBAction func onClickCancel(_ sender: UIBarButtonItem) {
        
        self.navigationController!.dismiss(animated: true) {
            self.navigationController = nil
        }
    }
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
