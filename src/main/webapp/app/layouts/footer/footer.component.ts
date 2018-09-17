import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'jhi-footer',
    templateUrl: './footer.component.html',
    styleUrls: ['footer.css']
})
export class FooterComponent implements OnInit {
    currentYear: number;

    ngOnInit(): void {
        this.currentYear = new Date().getFullYear();
    }
}
