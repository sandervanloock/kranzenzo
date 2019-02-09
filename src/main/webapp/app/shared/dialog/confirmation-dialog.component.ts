import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';

export interface DialogData {
    title: string;
    message: string;
}

@Component({
    selector: 'jhi-confirmation-dialog',
    template:
        '<h1 mat-dialog-title>{{data.title}}</h1>' +
        '<div mat-dialog-content>{{data.message}}</div>' +
        '<div mat-dialog-actions>' +
        '<button mat-button (click)="onNoClick()">Sluiten</button>' +
        '</div>'
})
export class ConfirmationDialogComponent {
    constructor(public dialogRef: MatDialogRef<ConfirmationDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: DialogData) {}

    onNoClick(): void {
        this.dialogRef.close();
    }
}
