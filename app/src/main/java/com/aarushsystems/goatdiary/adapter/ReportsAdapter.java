package com.aarushsystems.goatdiary.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aarushsystems.goatdiary.R;
import com.aarushsystems.goatdiary.model.Cell;
import com.aarushsystems.goatdiary.model.ColumnHeader;
import com.aarushsystems.goatdiary.model.RowHeader;
import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;

import java.util.ArrayList;

public class ReportsAdapter extends AbstractTableAdapter<ColumnHeader, RowHeader, Cell> {

    private String tableName;
    private int rowIndex = -1, colIndex = -1;
    private ArrayList<Integer> rowArrayList;

    public ReportsAdapter(Context context, String tableName) {
        super(context);
        this.tableName = tableName;
        this.rowArrayList = new ArrayList<>();
    }

    class MyCellViewHolder extends AbstractViewHolder {

        public final TextView cell_textview;
        public final View itemView;
        public int allRow, allCol;

        public MyCellViewHolder(View itemView) {
            super(itemView);
            cell_textview = itemView.findViewById(R.id.cell_data);
            this.itemView = itemView;
        }

        @Override
        public void setSelected(SelectionState selectionState) {
            super.setSelected(selectionState);
            if (selectionState == SelectionState.SELECTED) {
                if (cell_textview.getText().toString().contains("Total")) {
                    cell_textview.setTextColor(Color.BLACK);
                    cell_textview.setTypeface(Typeface.DEFAULT_BOLD);
                } else {
                    cell_textview.setTextColor(Color.BLACK);
                }
                if (allRow == rowIndex) {
                    cell_textview.setTextColor(Color.BLACK);
                    cell_textview.setTypeface(Typeface.DEFAULT_BOLD);
                }
            } else {
                if (cell_textview.getText().toString().contains("Total")) {
                    //Log.i("CUSTOM"," when total row array size = "+rowArrayList.size());
                    cell_textview.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                    cell_textview.setTypeface(Typeface.DEFAULT_BOLD);
                }
                if (rowArrayList.contains(allRow) && allCol >= colIndex) {
                    //Log.i("CUSTOM","row array list contains = "+allRow);
                    if (!cell_textview.getText().toString().isEmpty()) {
                        cell_textview.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                        cell_textview.setTypeface(Typeface.DEFAULT_BOLD);
                    }
                }
            }
            try {
                if (Integer.parseInt(cell_textview.getText().toString()) < 0) {
                    cell_textview.setTextColor(Color.RED);
                    //itemView.setBackgroundColor(Color.RED);
                }
            } catch (NumberFormatException e) {
                try {
                    if (Float.parseFloat(cell_textview.getText().toString()) < 0) {
                        cell_textview.setTextColor(Color.RED);
                        //itemView.setBackgroundColor(Color.RED);
                    }
                } catch (NumberFormatException e1) {

                }
            }
        }
    }


    @Override
    public AbstractViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_cell_layout, parent, false);
        return new MyCellViewHolder(layout);
    }

    @Override
    public void onBindCellViewHolder(AbstractViewHolder holder, Object cellItemModel, int
            columnPosition, int rowPosition) {
        Cell cell = (Cell) cellItemModel;

        MyCellViewHolder viewHolder = (MyCellViewHolder) holder;

        if (cell.getData().contains("Total")) {
            if (!rowArrayList.contains(rowPosition)) {
                rowArrayList.add(rowPosition);
                colIndex = columnPosition;
            }
        }
        viewHolder.allRow = rowPosition;
        viewHolder.allCol = columnPosition;
        viewHolder.cell_textview.setText(cell.getData());
        //viewHolder.cell_textview.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
        viewHolder.itemView.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
        viewHolder.cell_textview.requestLayout();
    }


    class MyColumnHeaderViewHolder extends AbstractViewHolder {

        public final TextView column_header_textView;
        public View itemView;

        public MyColumnHeaderViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            column_header_textView = (TextView) itemView.findViewById(R.id.column_header_textView);
        }

        @Override
        public void setSelected(SelectionState selectionState) {
            super.setSelected(selectionState);
        }
    }


    @Override
    public AbstractViewHolder onCreateColumnHeaderViewHolder(ViewGroup parent, int viewType) {

        View layout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.table_view_column_header_layout, parent, false);
        return new MyColumnHeaderViewHolder(layout);
    }


    @Override
    public void onBindColumnHeaderViewHolder(AbstractViewHolder holder, Object columnHeaderItemModel, int
            position) {
        ColumnHeader columnHeader = (ColumnHeader) columnHeaderItemModel;

        // Get the holder to update cell item text
        MyColumnHeaderViewHolder columnHeaderViewHolder = (MyColumnHeaderViewHolder) holder;
        columnHeaderViewHolder.column_header_textView.setText(columnHeader.getData());

        // If your TableView should have auto resize for cells & columns.
        // Then you should consider the below lines. Otherwise, you can ignore them.

        // It is necessary to remeasure itself.
        /*columnHeaderViewHolder.itemView.getLayoutParams().width = LinearLayout
                .LayoutParams.WRAP_CONTENT;
        columnHeaderViewHolder.itemView.requestLayout();*/
    }

    class MyRowHeaderViewHolder extends AbstractViewHolder {

        public final TextView row_header_textview;

        public MyRowHeaderViewHolder(View itemView) {
            super(itemView);
            row_header_textview = (TextView) itemView.findViewById(R.id.row_header_textview);
        }
    }


    @Override
    public AbstractViewHolder onCreateRowHeaderViewHolder(ViewGroup parent, int viewType) {

        View layout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.table_view_row_header_layout, parent, false);
        return new MyRowHeaderViewHolder(layout);
    }


    @Override
    public void onBindRowHeaderViewHolder(AbstractViewHolder holder, Object rowHeaderItemModel, int
            position) {
        RowHeader rowHeader = (RowHeader) rowHeaderItemModel;

        MyRowHeaderViewHolder rowHeaderViewHolder = (MyRowHeaderViewHolder) holder;
        rowHeaderViewHolder.row_header_textview.setText(rowHeader.getData());
    }

    @Override
    public View onCreateCornerView() {
        return LayoutInflater.from(mContext).inflate(R.layout.table_view_corner_layout, null);
    }

    /*@Override
    public View onCreateCornerView(ViewGroup parent) {
        // Get Corner xml layout
        return LayoutInflater.from(parent.getContext())
                .inflate(R.layout.table_view_corner_layout, parent, false);
    }*/

    @Override
    public int getColumnHeaderItemViewType(int columnPosition) {

        return 0;
    }

    @Override
    public int getRowHeaderItemViewType(int rowPosition) {
        // The unique ID for this type of row header item
        // If you have different items for Row Header View by Y (Row) position,
        // then you should fill this method to be able create different
        // type of RowHeaderViewHolder on "onCreateRowHeaderViewHolder"
        return 0;
    }

    @Override
    public int getCellItemViewType(int columnPosition) {
        return 0;
    }
}
