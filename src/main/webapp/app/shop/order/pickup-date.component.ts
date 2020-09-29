import {Component, OnInit} from '@angular/core';
import {NgbTimeStruct} from '@ng-bootstrap/ng-bootstrap';
import * as moment from 'moment';
import {Moment} from 'moment';

@Component( {
                selector: 'jhi-pickup-date', templateUrl: './pickup-date.component.html', styles: ['.date-time{ display: flex; align-items: center; justify-content: center}',]
            } )
export class PickupDateComponent implements OnInit {

    public date: Moment;
    public time: NgbTimeStruct = {hour: 13, minute: 30, second: 0};

    constructor() {
        this.date = moment();
    }

    ngOnInit() {
    }

    getSelectedDate(): Moment {
        const result = this.date;
        result.hour( this.time.hour );
        result.minute( this.time.minute );
        result.second( this.time.second );
        result.millisecond( 0 );
        return result;
    }

}
