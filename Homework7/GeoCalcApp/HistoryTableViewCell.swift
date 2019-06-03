//
//  HistoryTableViewCell.swift
//  GeoCalcApp
//
//  Created by Vamsy Priya Anne on 5/28/19.
//  Copyright © 2019 Vamsy Priya Anne. All rights reserved.
//

import UIKit

class HistoryTableViewCell: UITableViewCell {
    
    @IBOutlet weak var destPoint: UILabel!
    @IBOutlet weak var origPoint: UILabel!
    @IBOutlet weak var timestamp: UILabel!

    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
