//
//  GeoCalcTextField.swift
//  GeoCalcApp
//
//  Created by Vamsy Priya Anne on 5/21/19.
//  Copyright © 2019 Vamsy Priya Anne. All rights reserved.
//

import UIKit

class GeoCalcTextField: DecimalMinusTextField{

    override func awakeFromNib() {
        self.backgroundColor = UIColor.clear
        self.textColor = FOREGROUND_COLOR
        self.layer.borderWidth = 1.0
        self.layer.borderColor = FOREGROUND_COLOR.cgColor
        self.borderStyle = .roundedRect
        guard let ph = self.placeholder else {
            return
        }
        self.attributedPlaceholder = NSAttributedString(string: ph, attributes: [NSAttributedString.Key.foregroundColor : FOREGROUND_COLOR])
    }

}
