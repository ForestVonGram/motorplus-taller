import React from 'react';
import './DataTable.css';

export interface Column {
  key: string;
  header: string;
  render?: (value: any, row: any) => React.ReactNode;
}

interface DataTableProps {
  columns: Column[];
  data: any[];
  onAction?: (row: any) => void;
  actionLabel?: string;
}

const DataTable: React.FC<DataTableProps> = ({ 
  columns, 
  data, 
  onAction, 
  actionLabel = 'Ver Detalle' 
}) => {
  return (
    <div className="data-table-container">
      <table className="data-table">
        <thead>
          <tr>
            {columns.map((column) => (
              <th key={column.key}>{column.header}</th>
            ))}
            {onAction && <th></th>}
          </tr>
        </thead>
        <tbody>
          {data.map((row, index) => (
            <tr key={index}>
              {columns.map((column) => (
                <td key={column.key}>
                  {column.render ? column.render(row[column.key], row) : row[column.key]}
                </td>
              ))}
              {onAction && (
                <td>
                  <button 
                    className="action-button"
                    onClick={() => onAction(row)}
                  >
                    {actionLabel}
                  </button>
                </td>
              )}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default DataTable;
