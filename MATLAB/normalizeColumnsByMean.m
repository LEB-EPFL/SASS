function output = normalizeColumnsByMean( data )
%normalizeColumnsByMean Normalizes each column of matrix by dividing
% each element of column by the mean of the column.
output = zeros(size(data));
for i=1:size(data,2)
    output(:,i) = data(:,i) ./ mean(data(:,i));
end
end

