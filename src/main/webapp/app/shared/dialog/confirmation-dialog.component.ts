import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {Router} from '@angular/router';

export interface DialogData {
    title: string;
    message: string;
}

@Component({
    selector: 'jhi-confirmation-dialog',
    template:
        '<h1 mat-dialog-title>{{data.title}}</h1>' +
        '<mat-dialog-content>{{data.message}}</mat-dialog-content>' +
        '<mat-dialog-actions align="end">' +
        '<button mat-button (click)="onNoClick()">Sluiten</button>' +
        '</mat-dialog-actions>'
})
export class ConfirmationDialogComponent {
    constructor( private router: Router, public dialogRef: MatDialogRef<ConfirmationDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: DialogData) {}

    onNoClick(): void {
        this.dialogRef.close();
        this.router.navigate(['']);
    }
}
