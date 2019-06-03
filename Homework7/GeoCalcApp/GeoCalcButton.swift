//
//  GeoCalcButton.swift
//  GeoCalcApp
//
//  Created by Vamsy Priya Anne on 5/23/19.
//  Copyright © 2019 Vamsy Priya Anne. All rights reserved.
//

import UIKit

class GeoCalcButton: UIButton {

    override func awakeFromNib() {
        self.backgroundColor = FOREGROUND_COLOR
         self.tintColor = BACKGROUND_COLOR
        self.layer.cornerRadius = 10
    }
}
